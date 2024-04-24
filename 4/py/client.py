import socket
import random
import time

class Node:
    def __init__(self, port):
        self.port = port
        self.clock_time = random.uniform(-5.0, 5.0)
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def connect_to_server(self, server_port):
        self.client_socket.connect(("localhost", server_port))

    def send_time_to_server(self):
        self.client_socket.sendall(str(self.clock_time).encode())
        self.handle_server_messages()

    def handle_server_messages(self):
        while True:
            try:
                data = self.client_socket.recv(1024).decode()
                if data.startswith("REQUEST_TIME"):
                    self.client_socket.sendall(str(self.clock_time).encode())
                elif data.startswith("ADJUST:"):
                    adjustment = float(data.split(":")[1])
                    self.clock_time += adjustment
                    print(f"Adjusted time: {self.clock_time}")
            except Exception as e:
                print(f"Error: {e}")
                break

if __name__ == "__main__":
    node = Node(port=65432)
    node.connect_to_server(server_port=65432)
    while True:
        node.send_time_to_server()
        time.sleep(10)
