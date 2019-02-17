import { App } from './app';
import { IRomList, IState } from './types';

const $ = document.querySelector.bind(document);

export class UserInterface {
  private pcEl = $('#pc') as HTMLInputElement;
  private iEl = $('#i') as HTMLInputElement;
  private spEl = $('#sp') as HTMLInputElement;
  private dtEl = $('#dt') as HTMLInputElement;
  private stEl = $('#st') as HTMLInputElement;
  private romListEl = $('#rom-list') as HTMLSelectElement;

  private startBtn = $('#start') as HTMLButtonElement;
  private stopBtn = $('#stop') as HTMLButtonElement;
  private stepBtn = $('#step') as HTMLButtonElement;
  private resetBtn = $('#reset') as HTMLButtonElement;

  constructor(private app: App) {
    this.eventHandlers();
  }

  public updateRomList({ roms }: IRomList) {
    for (const romName of roms) {
      const optionEl = document.createElement('option');
      optionEl.value = romName;
      optionEl.textContent = romName;

      this.romListEl.appendChild(optionEl);
    }
  }

  public updateState({ pc, i, sp, dt, st, stack, v }: IState) {
    for (let x = 0; x < 16; x++) {
      const regEl = $('#v' + x) as HTMLInputElement;
      regEl.value = v[x].toString(16);
    }

    for (let x = 0; x < 16; x++) {
      const stackEl = $('#s' + x) as HTMLInputElement;
      stackEl.value = stack[x].toString(16);
    }

    this.pcEl.value = pc.toString(16);
    this.iEl.value = i.toString(16);
    this.spEl.value = sp.toString(16);
    this.dtEl.value = dt.toString(16);
    this.stEl.value = st.toString(16);
  }

  private eventHandlers() {
    const { ws } = this.app.socket;

    this.romListEl.onchange = () => {
      const selectedItem = this.romListEl.options[this.romListEl.selectedIndex]
        .value;

      ws.send(`loadrom ${selectedItem}`);
    };

    this.startBtn.onclick = () => {
      ws.send('start');
    };

    this.stopBtn.onclick = () => {
      ws.send('stop');
    };

    this.stepBtn.onclick = () => {
      ws.send('step');
    };

    this.resetBtn.onclick = () => {
      ws.send('reset');
    };
  }
}
