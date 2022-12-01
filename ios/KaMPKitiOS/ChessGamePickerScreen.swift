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

    func upsertGame(name: String, durationInMinutes: Int64, incrementsInSeconds: Int64, gameId: Int64) {
        if chessViewModel != nil {
            chessViewModel?.upsertChessGame(name: name,
                                            durationInMinutes: Int32(durationInMinutes),
                                            incrementInSeconds: Int32(incrementsInSeconds),
                                            id: gameId)
        }
    }

    func isValid(title: String, duration: Int32, increment: Int32) -> Bool {
        return chessViewModel?.isValid(name: title, durationInMinutes: duration, incrementInSeconds: increment) ?? false
    }

    func onDeleteGame(game: ChessGame) {
        chessViewModel?.deleteGame(chessGame: game)
    }
}

struct ChessGameListScreen: View {
    @StateObject
    var observableModel = ObservableChessGamePickerModel()

    var body: some View {
        ChessGamesListContent(
            loading: observableModel.loading,
            chessGames: observableModel.chessGames,
            error: observableModel.error,
            onDeleteGame: { observableModel.onDeleteGame(game: $0) },
            onSaveGame: { gameId, name, duration, increment in
                observableModel.upsertGame(
                    name: name, durationInMinutes: duration, incrementsInSeconds: increment, gameId: gameId
                )
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
    var onDeleteGame: (ChessGame) -> Void
    var onSaveGame: (Int64, String, Int64, Int64) -> Void
    var isValid: (String, Int32, Int32) -> Bool
    var inputConfig: InputConfig
    @State private var showingBottomSheet = false
    @State private var chessGame: ChessGame?

    var body: some View {
        ZStack {
            NavigationView {
                VStack {
                    if let chessGames = chessGames {
                        List(chessGames, id: \.id) { game in
                            NavigationLink {
                                ChessGameScreen(game: game)
                            } label: {
                                ChessGameRowView(game: game) {}
                            }.swipeActions(edge: .trailing) {
                                Button(action: { onDeleteGame(game) }, label: {
                                    Label("Delete", systemImage: "xmark.bin")
                                })
                                .tint(.red)
                            }.swipeActions(edge: .leading) {
                                Button(action: {
                                    chessGame = game
                                    showingBottomSheet.toggle()
                                }, label: {
                                    Label("Edit", systemImage: "xmark.bin")
                                })
                                .tint(.orange)
                            }
                        }
                    }
                    if let error = error {
                        Text(error)
                            .foregroundColor(.red)
                    }
                    Button("Custom game") {
                        showingBottomSheet.toggle()
                    }.sheet(isPresented: $showingBottomSheet) {
                        AddChessGameListContent(game: chessGame, inputConfig: inputConfig,
                                                onSaveGame: { gameId, name, duration, increment in
                            onSaveGame(gameId, name, duration ?? Int64(0), increment ?? Int64(0))
                            if gameId > -1 {
                                showingBottomSheet.toggle()
                            }
                        }, onCancel: {
                            showingBottomSheet.toggle() }, isValid: isValid).onDisappear(perform: {
                                chessGame = nil
                            })
                    }
                    if loading { Text("Loading..." + (chessGame?.name ?? "")) }
                }
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
    var name: String
    @State
    var increment: String
    @State
    var durationInMinutes: String
    @State
    var saveFlag: Bool
    var title: String

    init(game: ChessGame?, inputConfig: InputConfig,
         onSaveGame: @escaping (Int64, String, Int64?, Int64?) -> Void,
         onCancel: @escaping () -> Void, isValid: @escaping (String, Int32, Int32) -> Bool) {
        self.chessGame = game
        self.inputConfig = inputConfig
        self.onSaveGame = onSaveGame
        self.onCancel = onCancel
        self.increment = game != nil ? String(game!.increment) : ""
        self.name = game?.name ?? ""
        self.durationInMinutes = ""
        self.isValid = isValid
        self.saveFlag = game != nil
        if game == nil {
            self.title = "New game"
        } else {
            self.durationInMinutes = String(game!.time / 60 / 1000)
            self.title = "Edit game"
        }
    }

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Game details")) {
                    FloatingTextField(title: "Name", text: $name).onReceive(Just(name)) { newValue in
                        let filtered = newValue.prefix(Int(inputConfig.maxCharsName))
                        if newValue != filtered {
                            self.name = String(filtered)
                        }
                    }
                    FloatingTextField(title: "Duration (minutes)", text: $durationInMinutes)
                                .keyboardType(.numberPad)
                                .onReceive(Just(durationInMinutes)) { newValue in
                                    let filtered = newValue.prefix(Int(inputConfig.maxDigitsDuration)).filter {
                                        "0123456789".contains($0)
                                    }
                                    if filtered != newValue {
                                        self.durationInMinutes = filtered
                                    }
                                }
                    FloatingTextField(title: "Increment (seconds)", text: $increment)
                                .keyboardType(.numberPad)
                                .onReceive(Just(increment)) { newValue in
                                    let filtered = newValue.prefix(Int(inputConfig.maxDigitsIncrement)).filter {
                                        "0123456789".contains($0)
                                    }
                                    if filtered != newValue {
                                        self.increment = filtered
                                    }
                                }
                    if chessGame == nil {
                        Toggle(isOn: $saveFlag) {
                            Text("Save Game?")
                        }
                    }
                }
            }.toolbar(content: {
                let game = ChessGame(id: chessGame?.id ?? -1, name: name,
                                     time: (Int64(durationInMinutes) ?? 0) * 60 * 1000,
                                     increment: (Int64(increment) ?? 0))
                ToolbarItem {
                    if chessGame != nil {
                        Button(action: {
                            onSaveGame(game.id, name, Int64(durationInMinutes), Int64(increment))
                        }, label: { Text("Save") })
                    } else {
                        NavigationLink() {
                            ChessGameScreen(game: game)
                        } label: {
                            Text("Start")
                        }.disabled(!isValid(name, Int32(durationInMinutes) ?? 0, Int32(increment) ?? 0))
                            .onDisappear(perform: {
                            if saveFlag {
                                onSaveGame(game.id, name, Int64(durationInMinutes), Int64(increment))
                            }
                        })
                    }
                }
                    ToolbarItem(placement: .cancellationAction) {
                        Button("Cancel") { onCancel() }
                    }
            }).navigationBarTitle(title)
        }
    }
}

struct ChessGameRowView: View {
    var game: ChessGame
    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, content: {
                Text(game.name)
                    .font(.headline)
                Text("Duration: " + TimeUtilsKt.formatTime(game.time, showMillis: false))
                    .font(.subheadline)
                Text("Increment: " + String(game.increment))
                    .font(.subheadline)
            })
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
            onDeleteGame: { _ in },
            onSaveGame: {_, _, _, _ in},
            isValid: {_, _, _ in true},
            inputConfig: InputConfig(maxCharsName: 0, maxDigitsDuration: 0, maxDigitsIncrement: 0)
        )
    }
}

struct FloatingTextField: View {
    let title: String
    let text: Binding<String>

    var body: some View {
        ZStack(alignment: .leading) {
            Text(title)
                .foregroundColor(text.wrappedValue.isEmpty ? Color(.placeholderText) : .accentColor)
                .offset(y: text.wrappedValue.isEmpty ? 0 : -25)
                .scaleEffect(text.wrappedValue.isEmpty ? 1 : 0.75, anchor: .leading)
            TextField("", text: text)
        }
        .padding(.top, text.wrappedValue.isEmpty ? 0 : 15)
            .animation(.spring(response: 0.4, dampingFraction: 0.3))
    }
}
