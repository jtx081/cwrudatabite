class Histogram extends IVisual {
  constructor(data, binWidth, colorScheme) {
    super(data)
    this.binWidth = binWidth
    this.colorScheme = colorScheme
  }

  setBin(binWidth) {
    this.binWidth = binWidth
  }

  setColor(colorScheme) {
    this.colorScheme = colorScheme
  }
}  