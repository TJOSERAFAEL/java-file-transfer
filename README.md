# FileTransfer

Multi server-client file transfer system written in Java. Compatible with Linux, Windows and MacOS.

## What is FileTransfer?

FileTransfer allows the user to quickly set up a multiple server-client to send files from machine to machine with no effort. There is no need to follow a complex configuration. 

Each machine can act as a server-client, client or server only. A simple descriptive YAML configuration file is provided to tweak some features.

## How to use it

- Start FileTransfer, the server will be ready fo receiving incoming files.
- On the client machine, start FileTransfer
- Enter the hostname that you want to send files to
- Enter the file path

## Default configuration

- Default configuration can be found on the `configuration.yaml` file:

```
network: 
  buffer-size: 16384
server: 
  port: 9991
  storage-path: "C:\\file-transfer\\"
  enabled: true
client:
  timeout: 2000
  enabled: true
```

Make sure to adjust `storage-path` to a directory where you want to store the received files.

## License 

License GPLv2


