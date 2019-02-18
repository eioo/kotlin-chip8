# Kotlin-chip8

CHIP-8 emulator written in Kotlin & TypeScript. It uses WebSockets to stream data to browser.

There's no real benefit in using WebSockets and browser for graphics and controls, I made the project just for practice and lulz.

## Example of operation

![example](https://i.imgur.com/MNbwb7s.gif)

## Requirements

- Node.js
- JDK 11

## Installation

1. Make copy of `.env.example` to `.env` and fill with your details.

2. Run these commands to install and run client

```
cd src/client
npm install
npm start
```

3. Install Maven dependencies listed in `pom.xml`
4. Run `com.eioo.chip8.Main.main`
5. Navigate to [http://yourhost:port](http://yourhost:port) in browser ([http://localhost:8080](http://localhost:8080) by default)

## TODO

- [ ] Missing instructions
- [ ] Some instructions are incorrect (?) (Pong doesn't count scores properly, ball sometimes passes through top of game area)
- [x] Handling of keypresses
- [x] Sounds
  - [ ] Add duration for sound instead of fixed .mp3 
- [x] Heartbeat for socket. Sometimes you need to refresh the page so that the socket connects properly

## License

This project is licensed under the MIT License - see the LICENSE.md file for details
