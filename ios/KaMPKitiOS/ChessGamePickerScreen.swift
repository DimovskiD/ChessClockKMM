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
    var inputConfig: InputConfig = InputConfig(maxCharsName: 0, maxDigitsDuration: 0, maxDigitsIncrement: 0)

    @Published
    var loading = false

    @Published
    var chessGames: [ChessGame]?

    @Published
    var error: String?

    private var cancellables = [AnyCancellable]()

    func activate() {
        let chessViewModel = KotlinDependencies.shared.getChessGamePickerViewModel()
        self.inputConfig = chessViewModel.inputConfig
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

    func upsertGame(name: String, durationInMinutes: Int64, incrementsInSeconds: Int64, id: Int64) {
        if (chessViewModel != nil) {
            chessViewModel?.upsertChessGame(name: name,
                                            durationInMinutes: Int32(durationInMinutes),
                                            incrementInSeconds: Int32(incrementsInSeconds),
                                            id: id)
        }
    }
    
    func isValid(title: String, duration: Int32, increment: Int32) -> Bool {
        return chessViewModel?.isValid(name: title, durationInMinutes: duration, incrementInSeconds: increment) ?? false
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
            onSaveGame: { id, name, duration, increment in
                observableModel.upsertGame(name: name, durationInMinutes: duration, incrementsInSeconds: increment, id: id)
            }, isValid: observableModel.isValid,
            inputConfig: observableModel.inputConfig
        )
        .onAppear(perform: {
            observableModel.activate()
        })
        .onDisappear(perform: {
            observableModel.deactivate()
        })
    }
}

struct ChessGamesListContent: View {
    var loading: Bool
    var chessGames: [ChessGame]?
    var error: String?
    var onGameClick: (ChessGame) -> Void
    var onSaveGame: (Int64, String, Int64, Int64) -> Void
    var isValid: (String, Int32, Int32) -> Bool
    var inputConfig: InputConfig
    @State private var showingBottomSheet = false
    

    var body: some View {
        ZStack {
            VStack {
                if let chessGames = chessGames {
                    NavigationView {
                        List(chessGames, id: \.id) { game in
                            NavigationLink {
                                ChessGameScreen(game: game)
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
                    showingBottomSheet.toggle()
                }.sheet(isPresented: $showingBottomSheet) {
                    AddChessGameListContent(chessGame: nil,  inputConfig: inputConfig, onSaveGame: {id, name, duration, increment in
                        onSaveGame(id, name, duration ?? Int64(0), increment ?? Int64(0))
                    },onCancel: { showingBottomSheet.toggle() }, isValid: isValid)
                    //                                    .presentationDetents([.fraction(0.15)])
                }
                if loading { Text("Loading...") }
            }
        }
    }
}

struct AddChessGameListContent: View {
    var chessGame: ChessGame?
    var inputConfig: InputConfig
    var onSaveGame: (Int64, String, Int64?, Int64?) -> Void
    var onCancel: () -> Void
    var isValid: (String, Int32, Int32) -> Bool
    
    @State
    var title: String = ""
    @State
    var increment: String = ""
    @State
    var durationInMinutes: String = ""
    @State
    var saveFlag: Bool = false

    init(chessGame: ChessGame? = nil, inputConfig: InputConfig,
         onSaveGame: @escaping (Int64, String, Int64?, Int64?) -> Void, onCancel: @escaping () -> Void, isValid: @escaping (String, Int32, Int32) -> Bool) {
        self.chessGame = chessGame
        self.inputConfig = inputConfig
        self.onSaveGame = onSaveGame
        self.onCancel = onCancel
        self.increment = ""
        self.durationInMinutes = ""
        self.isValid = isValid
    }

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Game details")) {
                    TextField("Name", text: $title).onReceive(Just(title)) { newValue in
                        let filtered = newValue.prefix(Int(inputConfig.maxCharsName))
                        if newValue != filtered {
                            self.title = String(filtered)
                        }
                    }
                    TextField("Duration (minutes)", text: $durationInMinutes)
                                .keyboardType(.numberPad)
                                .onReceive(Just(durationInMinutes)) { newValue in
                                    let filtered = newValue.prefix(Int(inputConfig.maxDigitsDuration)).filter { "0123456789".contains($0) }
                                    if filtered != newValue {
                                        self.durationInMinutes = filtered
                                    }
                                }
                    TextField("Increment (seconds)", text: $increment)
                                .keyboardType(.numberPad)
                                .onReceive(Just(increment)) { newValue in
                                    let filtered = newValue.prefix(Int(inputConfig.maxDigitsIncrement)).filter { "0123456789".contains($0) }
                                    if filtered != newValue {
                                        self.increment = filtered
                                    }
                                }
                    Toggle(isOn: $saveFlag) {
                        Text("Save Game?")
                    }
                }
            }.toolbar(content: {
                let game = ChessGame(id: chessGame?.id ?? -1, name: title,
                                     time: (Int64(durationInMinutes) ?? 0) * 60 * 1000,
                                     increment: (Int64(increment) ?? 0))
                ToolbarItem {
                    NavigationLink() {
                        ChessGameScreen(game: game)
                    } label: {
                        Text("Start")
                    }.disabled(!isValid(title, Int32(durationInMinutes) ?? 0, Int32(increment) ?? 0)).onDisappear(perform: {
                        if saveFlag {
                            onSaveGame(game.id, game.name, Int64(durationInMinutes), Int64(increment))
                        }
                    })
                }
                    ToolbarItem(placement: .cancellationAction) {
                        Button("Cancel") { onCancel() }
                    }
                }).navigationBarTitle("New game")
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
            onSaveGame: {_,_,_,_ in},
            isValid: {_,_,_ in true},
            inputConfig: InputConfig(maxCharsName: 0,maxDigitsDuration: 0,maxDigitsIncrement: 0)
        )
    }
}
