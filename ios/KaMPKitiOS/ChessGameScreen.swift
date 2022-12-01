//
//  ChessGameScreen.swift
//  KaMPKitiOS
//
//  Created by Daniel Dimovski on 16.11.22.
//  Copyright © 2022 Touchlab. All rights reserved.
//

import Foundation
import Combine
import SwiftUI
import shared

private let log = koin.loggerWithTag(tag: "ChessGame")

class ObservableChessGameModel: ObservableObject {
    private var chessViewModel: ChessGameCallbackViewModel?
    @Published
    var chessGameState: ChessGameViewState?
    private var cancellables = [AnyCancellable]()
    func activate(game: ChessGame) {
        let chessViewModel = KotlinDependencies.shared.getChessGameViewModel(game: game)
        doPublish(chessViewModel.chessGameState) { [weak self] chessState in
            self?.chessGameState = chessState
        }.store(in: &cancellables)
        self.chessViewModel = chessViewModel
    }
    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
        chessViewModel?.clear()
        chessViewModel = nil
    }
    func getWinner() -> Player? {
        return chessViewModel?.getWinner()
    }
    func onPlayPausePressed() {
        chessViewModel?.playPauseClicked()
    }
    func switchPlayer() {
        chessViewModel?.switchPlayer()
    }
    func onRestartClicked() {
        chessViewModel?.onRestartClicked()
    }
}

struct ChessGameScreen: View {
    @StateObject
    var observableModel = ObservableChessGameModel()
    var game: ChessGame
    @State
    var showAlert = false

    var body: some View {
        ZStack {
            VStack {
                Rectangle()
                    .fill(.black)
                    .frame(width: UIScreen.main.bounds.height/2,
                           height: UIScreen.main.bounds.height/2,
                           alignment: Alignment.top)
                Rectangle()
                    .fill(.white)
                    .frame(width: UIScreen.main.bounds.height/2,
                           height: UIScreen.main.bounds.height/2,
                           alignment: Alignment.top)
            }.onTapGesture {
                observableModel.switchPlayer()
            }
            VStack {
                ChessGamePlayerComponent(
                    timeRemaining: observableModel.chessGameState?.playerTwo.timeInMillis ?? 0,
                    movesMade: observableModel.chessGameState?.playerTwo.movesMade ?? 0,
                    color: .white
                ).rotationEffect(Angle(degrees: 180))
                ChessGamePlayerComponent(
                    timeRemaining: observableModel.chessGameState?.playerOne.timeInMillis ?? 0,
                    movesMade: observableModel.chessGameState?.playerOne.movesMade ?? 0,
                    color: .black
                )
            }.onTapGesture {
                observableModel.switchPlayer()
            }
            HStack {
                if observableModel.chessGameState?.gameState == GameState.finished {
                    Button(action: { observableModel.onRestartClicked() }, label: {
                        Image(uiImage: UIImage(systemName: "restart")!).padding()
                    }).background(Color.gray).cornerRadius(25).onAppear(perform: {
                        showAlert = true
                    }).alert(isPresented: $showAlert) {
                        Alert(title: Text((observableModel.getWinner()?.playerColor.name ?? "") + " player wins"))
                    }
                } else {
                    Button(action: { observableModel.onPlayPausePressed() }, label: {
                        if observableModel.chessGameState?.gameState == GameState.resumed {
                            Image(uiImage: UIImage(systemName: "pause.fill")!).padding()
                        } else {
                            Image(uiImage: UIImage(systemName: "play.fill")!).padding()
                        }
                    }).background(Color.gray).cornerRadius(25)
                    if observableModel.chessGameState?.gameState == GameState.paused {
                        Button(action: { observableModel.onRestartClicked() }, label: {
                            Image(uiImage: UIImage(systemName: "restart")!).padding()
                        }).background(Color.gray).cornerRadius(25)
                    }
                }
            }
        }
        .onAppear(perform: {
            observableModel.activate(game: game)
        })
        .onDisappear(perform: {
            observableModel.deactivate()
        })
    }
}

struct ChessGamePlayerComponent: View {
    var timeRemaining: Int64
    var movesMade: Int32
    var color: Color

    var body: some View {
        VStack {
            Text(TimeUtilsKt.formatTime(
                timeRemaining, showMillis: true
            )).foregroundColor(color).font(.largeTitle)
            HStack {
                Text("Total moves: " + String(movesMade))
                    .foregroundColor(color)
            }.frame(alignment: .bottom)
        }.frame(
            width: UIScreen.main.bounds.width,
            height: UIScreen.main.bounds.height/2,
            alignment: Alignment.center
        )
    }
}
