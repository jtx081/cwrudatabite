class HeatMap extends IVisual {
  constructor(dataSet, colorScheme) {
    super()
    this.dataSet = dataSet
    this.colorScheme = colorScheme
  }
  
  setColor(color) {
    this.colorScheme = color
  }
}
