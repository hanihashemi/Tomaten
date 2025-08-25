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
        VStack {
            Text("Tomaten iOS")
                .font(.title)
                .padding()
            
            Text("SharedMainScreen is now available!")
                .font(.headline)
                .padding()
            
            Text("You can use IOSMainScreen from the shared module")
                .font(.body)
                .padding()
                .multilineTextAlignment(.center)
            
            Text("To integrate:")
                .font(.headline)
                .padding()
            
            VStack(alignment: .leading, spacing: 8) {
                Text("1. Use ComposeUIViewController in your iOS app")
                Text("2. Call IOSMainScreen composable function")
                Text("3. Pass timer state and actions from iOS ViewModel")
            }
            .font(.caption)
            .padding()
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
