import socket
import threading
import random

class MasterNode:
    def __init__(self, port):
        self.port = port
        self.clock_time = 0.0
        self.nodes = []
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.bind(("localhost", port))
        self.server_socket.listen(5)

    def add_node(self, connection, address):
        self.nodes.append((connection, address))

    def start_server(self):
        print(f"Master node listening on port {self.port}...")
        while True:
            connection, address = self.server_socket.accept()
            print(f"Connected to client at {address}")
            self.add_node(connection, address)
            threading.Thread(target=self.handle_client, args=(connection,)).start()

    def handle_client(self, connection):
        while True:
            try:
                data = connection.recv(1024).decode()
                if data:
                    if data == "SYNC":
                        self.synchronize_clocks()
                    else:
                        client_time = float(data)
                        self.clock_time += random.gauss(0, 0.1)
                        print(f"Received time from client: {client_time}")
            except Exception as e:
                print(f"Error: {e}")
                break

    def synchronize_clocks(self):
        times = [self.clock_time]
        for connection, _ in self.nodes:
            connection.sendall("REQUEST_TIME".encode())
            client_time = float(connection.recv(1024).decode())
            times.append(client_time)

        average_time = sum(times) / len(times)

        for connection, _ in self.nodes:
            adjustment = average_time - times[self.nodes.index((connection, _)) + 1]
            connection.sendall(f"ADJUST:{adjustment}".encode())

        self.clock_time += average_time - self.clock_time
        print(f"New master time: {self.clock_time}")

if __name__ == "__main__":
    master_node = MasterNode(port=65432)
    master_node.start_server()
