import { App, IState } from './app';
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

    if (data.length === 64 * 32) {
      return this.app.gfx.drawGraphics(data);
    }

    // We got variables
    const vars = data.split(';');
    const state: IState = {
      pc: parseInt(vars[0], 10),
      i: parseInt(vars[1], 10),
      sp: parseInt(vars[2], 10),
      dt: parseInt(vars[3], 10),
      st: parseInt(vars[4], 10),
      stack: vars[5].split(',').map(x => parseInt(x, 10)),
      v: vars[6].split(',').map(x => parseInt(x, 10)),
    };

    this.app.updateState(state);
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
