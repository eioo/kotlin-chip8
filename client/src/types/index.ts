export interface IState {
  emustate: string;
  pc: number;
  i: number;
  sp: number;
  dt: number;
  st: number;
  stack: number[];
  v: number[];
}

export interface IRomList {
  roms: string[];
}
