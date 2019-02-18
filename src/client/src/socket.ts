import { App } from './app';
import config from './config';
import { IRomList, IState } from './types';

const socketStatusEl = document.querySelector(
  '#socket-status'
) as HTMLSpanElement;

export class Socket {
  public ws: WebSocket;

  constructor(private app: App) {
    this.createSocket();
  }

  private createSocket() {
    socketStatusEl.textContent = 'Connecting';
    this.ws = new WebSocket(`ws://${config.host}:${config.port}`);
    this.setup();
  }

  private setup() {
    this.ws.onmessage = this.onMessage;
    this.ws.onopen = this.onOpen;
    this.ws.onclose = this.onClose;
    this.ws.onerror = this.onError;
  }

  private onMessage = (ev: Event) => {
    const { data } = ev;

    if (data.length === 64 * 32 && ['0', '1'].includes(data[0])) {
      return this.app.gfx.drawGraphics(data);
    }

    const json = JSON.parse(data);
    const keys = Object.keys(json);

    if (keys.includes('emustate')) {
      this.app.ui.updateState(json as IState);
    }

    if (keys.includes('roms')) {
      this.app.ui.updateRomList(json as IRomList);
    }
  };

  private onOpen = () => {
    socketStatusEl.textContent = 'Connected';
    console.log('Connected to server');
  };

  private onClose(ev: Event) {
    if (ev.code === 1006) {
      return;
    }

    console.log('Closed connection to server');
  }

  private onError = () => {
    this.reconnect();
  };

  private reconnect() {
    this.createSocket();
  }
}
