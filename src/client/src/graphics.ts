export class Graphics {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;

  private scale = 5;

  constructor() {
    this.canvas = document.querySelector('canvas') as HTMLCanvasElement;
    this.canvas.width = 64 * this.scale;
    this.canvas.height = 32 * this.scale;
    this.ctx = this.canvas.getContext('2d') as CanvasRenderingContext2D;
  }

  public drawGraphics = (data: string) => {
    this.ctx.fillStyle = 'lightgray';
    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);

    const display = Array.from(data);

    display.forEach((char, i) => {
      const x = i % 64;
      const y = Math.floor(i / 64);

      this.ctx.fillStyle = char === '0' ? '#000' : '#fff';
      this.ctx.fillRect(x * this.scale, y * this.scale, this.scale, this.scale);
    });

    for (let x = 0; x < data.length; x++) {
      const y = Math.floor(x / 32);
    }
  };
}
