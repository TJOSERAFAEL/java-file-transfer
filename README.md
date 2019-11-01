# FileTransfer

Multi client file transfer system written in Java. Compatible with Linux, Windows and MacOS.

## How to use it

- Start FileTransfer, the server will be ready fo receiving incoming files.
- On the client machine, start FileTransfer
- Enter the hostname that you want to send files
- Enter the file path

## Default configuration

- Default configuration can be found on the `configuration.yaml` file:

```
network: 
  buffer-size: 16384
server: 
  port: 9991
  storage-path: "C:\\file-transfer\\"
client:
  timeout: 2000
```

Make sure to adjust `storage-path` to a directory where you want to store the received files.

