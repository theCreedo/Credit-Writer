function addTable() {
    var myTableDiv = document.getElementById("metric_results")
    var table = document.createElement('TABLE')
    var tableBody = document.createElement('TBODY')

    table.border = '1'
    table.appendChild(tableBody);

    var heading = new Array();
    heading[0] = "Artist"
    heading[1] = "Location"
    heading[2] = "Song"

    var stock = new Array()
    stock[0] = new Array("ACDC", "Austin", "Hello")

    //TABLE COLUMNS
    var tr = document.createElement('TR');
    tableBody.appendChild(tr);
    for (i = 0; i < heading.length; i++) {
        var th = document.createElement('TH')
        th.width = '75';
        th.appendChild(document.createTextNode(heading[i]));
        tr.appendChild(th);
    }

    //TABLE ROWS
    for (i = 0; i < stock.length; i++) {
        var tr = document.createElement('TR');
        for (j = 0; j < stock[i].length; j++) {
            var td = document.createElement('TD')
            td.appendChild(document.createTextNode(stock[i][j]));
            tr.appendChild(td)
        }
        tableBody.appendChild(tr);
    }  
    myTableDiv.appendChild(table)
}