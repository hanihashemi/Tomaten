//
//  ContentView.swift
//  iosApp
//
//  Created by Hanialhassan Hashemifar on 23.08.25.
//

import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return ComposeUIViewControllerKt.createMainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    ContentView()
}
