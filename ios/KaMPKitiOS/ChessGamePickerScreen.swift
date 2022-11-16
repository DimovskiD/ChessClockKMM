//
//  ChessGamePickerScreen.swift
//  KaMPKitiOS
//
//  Created by Daniel Dimovski on 16.11.22.
//  Copyright © 2022 Touchlab. All rights reserved.
//

import Foundation
import Combine
import SwiftUI
import shared

private let log = koin.loggerWithTag(tag: "ChessGamePicker")

class ObservableChessGamePickerModel: ObservableObject {

    private var chessViewModel: ChessGamePickerCallbackViewModel?

    @Published
    var loading = false

    @Published
    var chessGames: [ChessGame]?

    @Published
    var error: String?

    private var cancellables = [AnyCancellable]()

    func activate() {
        let chessViewModel = KotlinDependencies.shared.getChessGamePickerViewModel()
        doPublish(chessViewModel.chessGames) { [weak self] chessState in
            self?.loading = chessState.isLoading
            self?.chessGames = chessState.chessGames
            self?.error = chessState.error

            if let chessGames = chessState.chessGames {
                log.d(message: {"View updating with \(chessGames.count) chess games"})
            }
            if let errorMessage = chessState.error {
                log.e(message: {"Displaying chess game error: \(errorMessage)"})
            }
        }.store(in: &cancellables)
        self.chessViewModel = chessViewModel
    }

    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()

        chessViewModel?.clear()
        chessViewModel = nil
    }

    func onAddNewClicked() {
        chessViewModel?.onCreateNewGameClicked()
    }
    func onGameClick(game: ChessGame) {}
}

struct ChessGameListScreen: View {
    @StateObject
    var observableModel = ObservableChessGamePickerModel()

    var body: some View {
        ChessGamesListContent(
            loading: observableModel.loading,
            chessGames: observableModel.chessGames,
            error: observableModel.error,
            onGameClick: { observableModel.onGameClick(game: $0) },
            onAddNewClick: { observableModel.onAddNewClicked() }
        )
        .onAppear(perform: {
            observableModel.activate()
        })
        .onDisappear(perform: {
            observableModel.deactivate()
        })
    }
}


struct ChessGameListContent: View {
    var loading: Bool
    var games: [ChessGame]?
    var error: String?
    var onGameClick: (ChessGame) -> Void
    var onAddNewClick: () -> Void

    var body: some View {
        ZStack {
            VStack {
                if let games = games {
                    List(games, id: \.id) { game in
                        ChessGameRowView(game: game) {
                            onGameClick(game)
                        }
                    }
                }
                if let error = error {
                    Text(error)
                        .foregroundColor(.red)
                }
                Button("Add new") {
                    onAddNewClick()
                }
            }
            if loading { Text("Loading...") }
        }
    }
}

struct ChessGamesListContent: View {
    var loading: Bool
    var chessGames: [ChessGame]?
    var error: String?
    var onGameClick: (ChessGame) -> Void
    var onAddNewClick: () -> Void
    var body: some View {
        ZStack {
            VStack {
                if let chessGames = chessGames {
                    NavigationView {
                        List(chessGames, id: \.id) { game in
                            NavigationLink {
                                ChessGameScreen()
                            } label: {
                                    ChessGameRowView(game: game) {
                                        onGameClick(game)
                                    }
                            }
                        }
                    }
                }
                if let error = error {
                    Text(error)
                        .foregroundColor(.red)
                }
                Button("Add new") {
                    onAddNewClick()
                }
            }
            if loading { Text("Loading...") }
        }
    }
}

struct ChessGameRowView: View {
    var game: ChessGame
    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack {
                Text(game.name)
                    .padding(4.0)
            }
        }
    }
}

struct ChessGamePickerScreen_Previews: PreviewProvider {
    static var previews: some View {
        ChessGamesListContent(
            loading: false,
            chessGames: [
                ChessGame(id: 1, name: "Test", time: 2000, increment: 1),
                ChessGame(id: 2, name: "New", time: 30000, increment: 2)
            ],
            error: nil,
            onGameClick: { _ in },
            onAddNewClick: {}
        )
    }
}
