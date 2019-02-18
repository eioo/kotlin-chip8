import { App } from './app';
import config from './config';
import { IRomList, IState } from './types';

export class Socket {
  public ws: WebSocket;
  private connectingText = 'Waiting for server';
  private connectedText = 'Connected';

  constructor(private app: App) {
    this.app.ui.setSocketStatus(this.connectingText);
    this.createSocket();
  }

  private createSocket = () => {
    this.ws = new WebSocket(`ws://${config.host}:${config.port}`);
    this.setup();
  };

  private setup() {
    this.ws.onmessage = this.onMessage;
    this.ws.onopen = this.onOpen;
    this.ws.onclose = this.onClose;
  }

  private onMessage = (ev: Event) => {
    const { data } = ev;

    if (data.length === 64 * 32 && ['0', '1'].includes(data[0])) {
      return this.app.gfx.drawGraphics(data);
    }

    const json = JSON.parse(data);
    const keys = Object.keys(json);

    if (keys.includes('emustate')) {
      return this.app.ui.updateState(json as IState);
    }

    if (keys.includes('roms')) {
      this.app.ui.updateRomList(json as IRomList);
    }
  };

  private onOpen = () => {
    this.app.ui.setSocketStatus(this.connectedText);
    this.app.ui.setEmulatorStatus('Please select rom');
    this.app.ui.enableRomSelect();

    console.log('Connected to server');
  };

  private onClose = () => {
    this.app.ui.reset();
    this.app.ui.setSocketStatus(this.connectingText);
    this.app.ui.setEmulatorStatus('-');

    setTimeout(() => {
      this.createSocket();
    }, 500);
  };
}
