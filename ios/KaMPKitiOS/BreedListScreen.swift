//
//  BreedListView.swift
//  KaMPKitiOS
//
//  Created by Russell Wolf on 7/26/21.
//  Copyright © 2021 Touchlab. All rights reserved.
//

import Combine
import SwiftUI
import shared

private let log = koin.loggerWithTag(tag: "ViewController")

class ObservableBreedModel: ObservableObject {
    private var viewModel: BreedCallbackViewModel?
    
    @Published
    var loading = false

    @Published
    var breeds: [Breed]?
    
    @Published
    var error: String?

    private var cancellables = [AnyCancellable]()

    func activate() {
        let viewModel = KotlinDependencies.shared.getBreedViewModel()
        
        doPublish(viewModel.breeds) { [weak self] dogsState in
            self?.loading = dogsState.isLoading
            self?.breeds = dogsState.breeds
            self?.error = dogsState.error

            if let breeds = dogsState.breeds {
                log.d(message: {"View updating with \(breeds.count) breeds"})
            }
            if let errorMessage = dogsState.error {
                log.e(message: {"Displaying error: \(errorMessage)"})
            }
        }.store(in: &cancellables)
        
        self.viewModel = viewModel
        }

    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()

        viewModel?.clear()
        viewModel = nil
    }

    func onBreedFavorite(_ breed: Breed) {
        viewModel?.updateBreedFavorite(breed: breed)
    }

    func refresh() {
        viewModel?.refreshBreeds()
    }
}

class ObservableChessGamesModel: ObservableObject {

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
    var observableModel = ObservableChessGamesModel()

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

struct BreedListScreen: View {
    @StateObject
    var observableModel = ObservableBreedModel()

    var body: some View {
        BreedListContent(
            loading: observableModel.loading,
            breeds: observableModel.breeds,
            error: observableModel.error,
            onBreedFavorite: { observableModel.onBreedFavorite($0) },
            refresh: { observableModel.refresh() }
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

struct BreedListContent: View {
    var loading: Bool
    var breeds: [Breed]?
    var error: String?
    var onBreedFavorite: (Breed) -> Void
    var refresh: () -> Void

    var body: some View {
        ZStack {
            VStack {
                if let breeds = breeds {
                    List(breeds, id: \.id) { breed in
                        BreedRowView(breed: breed) {
                            onBreedFavorite(breed)
                        }
                    }
                }
                if let error = error {
                    Text(error)
                        .foregroundColor(.red)
                }
                Button("Refresh") {
                    refresh()
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
                    List(chessGames, id: \.id) { game in
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

struct BreedRowView: View {
    var breed: Breed
    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack {
                Text(breed.name)
                    .padding(4.0)
                Spacer()
                Image(systemName: (!breed.favorite) ? "heart" : "heart.fill")
                    .padding(4.0)
            }
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

struct BreedListScreen_Previews: PreviewProvider {
    static var previews: some View {
        BreedListContent(
            loading: false,
            breeds: [
                Breed(id: 0, name: "appenzeller", favorite: false),
                Breed(id: 1, name: "australian", favorite: true)
            ],
            error: nil,
            onBreedFavorite: { _ in },
            refresh: {}
        )
    }
}
