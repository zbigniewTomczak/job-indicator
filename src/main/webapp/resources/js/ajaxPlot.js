function loadPlotData() {
    	console.log("ssuccess");

        var options = {
                lines: { show: true },
                points: { show: true },
                xaxis: { tickDecimals: 0, tickSize: 1 }
            };
        var data = [];
        var placeholder = $("#placeholder");

    	// find the URL in the link right next to us 
        var dataurl = $('#data_link').attr('href');

        // then fetch the data with jQuery
        function onDataReceived(series) {
            // extract the first coordinate pair so you can see that
            // data is now an ordinary Javascript object
            var firstcoordinate = '(' + series.data[0][0] + ', ' + series.data[0][1] + ')';

            data.push(series);
            
            // and plot all we got
            $.plot(placeholder, data, options);
         }
        
        $.ajax({
            url: dataurl,
            method: 'GET',
            dataType: 'json',
            success: onDataReceived,
            global: false
        });
    };
    
    