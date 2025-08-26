//
//  SharedUIExample.swift
//  iosApp
//
//  Example of how to use the shared Compose UI in iOS
//

import SwiftUI
import sharedKit

struct SharedUIExample: View {
    var body: some View {
        // This is how you would integrate the shared Compose UI
        ComposeMainView()
            .ignoresSafeArea(.all)
    }
}

struct ComposeMainView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // This will use the shared Compose UI with the same layout and styling as Android
        // The exact method call depends on how Kotlin exports the function to Swift
        
        // Option 1: Direct function call (if available)
        // return MainViewController()
        
        // Option 2: Through Kotlin package namespace
        // return IOSMainViewControllerKt.MainViewController()
        
        // Option 3: Using ObjC name
        // return createMainViewController()
        
        // For now, return a placeholder
        return UIViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Handle updates if needed
    }
}

// Usage in your main ContentView:
/*
struct ContentView: View {
    var body: some View {
        SharedUIExample() // This will show the shared Compose UI
    }
}
*/