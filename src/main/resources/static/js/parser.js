// Save a blob to the browser as a string
function storeBlob(myBlob) {

    const reader = new FileReader();
    reader.onload = (event) => {
        localStorage.setItem('dataString', event.target.result);
    }

    reader.readAsText(myBlob);
}

// Get the labels for a graph given data as a string
function getLabels(dataString) {

    const rows = dataString.split('\n');
    const labels = rows[0].split(',');
    return labels;
}

// Get the data as an array of arrays
function getData(dataString) {

    const rows = dataString.split('\n').slice(1);

    var i;
    for (i = 0; i < rows.length; i++) {
        rows[i] = rows[i].split(',');
    }

    var columns = new Array();
    var j;
    for (j = 0; j < rows[0].length; j++) {
        var k;
        var oneColumn = new Array();
        for (k = 0; k < rows.length; k++) {
            oneColumn.push(rows[k][j]);
        }
        columns.push(oneColumn);
    }

    return columns;
}

// Format the "datasets:" section of a polar area chart, histogram, or line graph
function formatPHLDatasets(data, index) {

    return {
        data: data[index],
        borderWidth: 1,
        borderColor: '#777'
    };
}

// Format the "datasets:" section of a scatter plot
function formatSDatasets(data, index) {

    return {
        x: data[0][index],
        y: data[1][index]
    }
}

// Format the "data:" section of a box plot
function formatBDatasets(data, labels) {

    var jsonData = [];

    var i;
    for (i = 0; i < data.length; i++) {
        jsonData.push({
            x: labels[i],
            y: data[i]
        });
    }

    return jsonData;
}

// Format the "series:" section of a box plot
function formatHDatasets(data, labels) {

    var jsonData = [];

    var i;
    for (i = 0; i < data.length; i++) {
        jsonData.push({
            name: labels[i],
            data: data[i]
        });
    }

    return jsonData;
}