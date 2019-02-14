import { App } from './app';
import config from './config';

const socketStatusEl = document.querySelector(
  '#socket-status'
) as HTMLSpanElement;

export class Socket {
  public ws: WebSocket | null;

  constructor(private app: App) {
    this.createSocket();
  }

  private createSocket() {
    socketStatusEl.textContent = 'Connecting';
    this.ws = new WebSocket(`ws://${config.host}:${config.port}`);
    this.setup();
  }

  private setup() {
    if (!this.ws) {
      return;
    }

    this.ws.onmessage = this.onMessage;
    this.ws.onopen = this.onOpen;
    this.ws.onclose = this.onClose;
    this.ws.onerror = this.onError;
  }

  private onMessage = (ev: Event) => {
    const { data } = ev;
    this.app.gfx.drawGraphics(data);
  };

  private onOpen = () => {
    socketStatusEl.textContent = 'Connected';
    console.log('Connected to server');
  };

  private onClose(ev: Event) {
    this.ws = null;

    if (ev.code === 1006) {
      return;
    }

    console.log('Closed connection to server');
  }

  private onError = () => {
    this.reconnect();
  };

  private reconnect() {
    socketStatusEl.textContent = 'Retrying';

    let counter = 0;

    const dotter = setInterval(() => {
      socketStatusEl.textContent += '.';
      counter++;

      if (counter >= 3) {
        clearInterval(dotter);
      }
    }, 1000);

    setTimeout(() => {
      this.createSocket();
    }, 4e3);
  }
}
