#!/usr/bin/env python3
import http.server
import socketserver
import os
import json

PORT = 8080
DIRECTORY = os.path.dirname(os.path.abspath(__file__)) + "/../ota"

class OTAHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        print(f"Update request from device: {self.path}")
        return super().do_GET()

def start_server():
    os.chdir(DIRECTORY)
    with socketserver.TCPServer(("", PORT), OTAHandler) as httpd:
        print(f"--- NovaOS OTA Mock Server ---")
        print(f"Serving files from: {DIRECTORY}")
        print(f"Local URL: http://0.0.0.0:{PORT}/updates.json")
        print(f"Press Ctrl+C to stop.")
        httpd.serve_forever()

if __name__ == "__main__":
    # Ensure builds directory exists
    os.makedirs("builds", exist_ok=True)
    start_server()
