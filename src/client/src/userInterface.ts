import { App } from './app';
import { IRomList, IState } from './types';

const $ = document.querySelector.bind(document);
const $all = document.querySelectorAll.bind(document);

export class UserInterface {
  private pcEl = $('#pc') as HTMLInputElement;
  private iEl = $('#i') as HTMLInputElement;
  private spEl = $('#sp') as HTMLInputElement;
  private dtEl = $('#dt') as HTMLInputElement;
  private stEl = $('#st') as HTMLInputElement;

  private socketStatusEl = $('#socket-status') as HTMLSpanElement;
  private emuStatusEl = $('#emulator-status') as HTMLDivElement;
  private romListEl = $('#rom-list') as HTMLSelectElement;
  private startBtn = $('#start') as HTMLButtonElement;
  private stopBtn = $('#stop') as HTMLButtonElement;
  private resetBtn = $('#reset') as HTMLButtonElement;
  private cpuFreqSlider = $('#cpu-frequency') as HTMLInputElement;
  private cpuFreqLabel = $('#cpu-frequency-label') as HTMLSpanElement;
  private varElements = Array.from(
    $all('.variables input[type="text"]')
  ) as HTMLInputElement[];

  private defaultCpuFreq = 60;

  constructor(private app: App) {
    this.eventHandlers();
  }

  public reset() {
    this.romListEl.setAttribute('disabled', '');
    this.romListEl.value = '';
    this.startBtn.setAttribute('disabled', '');
    this.stopBtn.setAttribute('disabled', '');
    this.resetBtn.setAttribute('disabled', '');
    this.cpuFreqSlider.setAttribute('disabled', '');
    this.cpuFreqSlider.value = this.defaultCpuFreq.toString();
    this.cpuFreqLabel.textContent = `${this.defaultCpuFreq}Hz`;
    this.varElements.forEach(el => {
      el.value = '0';
    });

    this.app.gfx.clearGraphics();
  }

  public setSocketStatus(text: string) {
    this.socketStatusEl.textContent = text;
  }

  public setEmulatorStatus(text: string) {
    this.emuStatusEl.textContent = text;
  }

  public disableRomSelect() {}

  public enableRomSelect() {
    this.emuStatusEl.textContent = 'Please select rom';
    this.romListEl.removeAttribute('disabled');
  }

  public enableEmulatorControls() {
    this.startBtn.removeAttribute('disabled');
    this.stopBtn.removeAttribute('disabled');
    this.resetBtn.removeAttribute('disabled');
    this.cpuFreqSlider.removeAttribute('disabled');
  }

  public updateRomList({ roms }: IRomList) {
    for (const romName of roms) {
      const optionEl = document.createElement('option');
      optionEl.value = romName;
      optionEl.textContent = romName;

      this.romListEl.appendChild(optionEl);
    }
  }

  public updateState({ emustate, pc, i, sp, dt, st, stack, v }: IState) {
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
    this.updateEmulatorStatus(emustate ? 'Running' : 'Stopped');
  }

  public updateEmulatorStatus(newStatus: string) {
    this.emuStatusEl.textContent = newStatus;
  }

  private eventHandlers() {
    this.romListEl.onchange = () => {
      const selectedItem = this.romListEl.options[this.romListEl.selectedIndex]
        .value;

      this.app.socket.ws.send(`loadrom ${selectedItem}`);
      this.enableEmulatorControls();
    };

    this.startBtn.onclick = () => {
      this.app.socket.ws.send('start');
    };

    this.stopBtn.onclick = () => {
      this.app.socket.ws.send('stop');
    };

    this.resetBtn.onclick = () => {
      this.app.socket.ws.send('reset');
    };

    this.cpuFreqSlider.oninput = () => {
      this.cpuFreqLabel.textContent = this.cpuFreqSlider.value + 'Hz';
    };

    this.cpuFreqSlider.onchange = () => {
      this.app.socket.ws.send(`changefreq ${this.cpuFreqSlider.value}`);
    };
  }
}
