# Kotlin-chip8

CHIP-8 emulator written in Kotlin & TypeScript. It uses WebSockets to stream data to browser.

There's no real benefit in using WebSockets and browser for graphics and controls, I made the project just for practice and lulz.

## Example of operation

![example](https://raw.githubusercontent.com/eioo/kotlin-chip8/master/github/example.gif)

## Installation

1. Make copy of `client/.env.example` to `client/.env` and fill with your details. You also need to change port in `com.eioo.chip8.Main.main` if you change it in `.env` (Default is 8080).

2. Run these commands to install and run client

```
cd client
npm install
npm start
```

3. Install Maven dependencies listed in `pom.xml`
4. Run `com.eioo.chip8.Main.main`
5. Navigate to [http://yourhost:port](http://yourhost:port) in browser ([http://localhost:8080](http://localhost:8080) by default)

## TODO

- Some instructions are still missing (Pong runs fine though!)
- Handling of keypresses
- Sounds
- Heartbeat for socket. Sometimes you need to refresh the page so that the socket connects properly

## License

This project is licensed under the MIT License - see the LICENSE.md file for details
