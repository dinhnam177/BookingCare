let optionsDonutForm = {
    series: [70, 30],
    labels: ['Male', 'Female'],
    colors: ['#435ebe', '#55c6e8'],
    chart: {
        type: 'donut',
        width: '100%',
        height: '350px'
    },
    legend: {
        position: 'bottom'
    },
    plotOptions: {
        pie: {
            donut: {
                size: '30%'
            }
        }
    }
}

let optionsBarForm = {
    annotations: {
        position: 'back'
    },
    dataLabels: {
        enabled: false
    },
    chart: {
        type: 'bar',
        height: 300
    },
    fill: {
        opacity: 1
    },
    plotOptions: {},
    series: [{
        name: 'sales',
        data: [9, 20, 30, 20, 10, 20, 30, 20, 10, 20, 30, 20]
    }],
    colors: '#435ebe',
    xaxis: {
        categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    },
}

let optionsAreaForm = {
    series: [{
        name: 'series1',
        data: [510, 800, 600, 430, 540, 340, 605, 805, 430, 540, 540, 605]
    },
        {
            name: 'series2',
            data: [400, 500, 900, 630, 540, 440, 405, 705, 930, 840, 540, 405]
        }],
    chart: {
        height: 300,
        type: 'area',
        toolbar: {
            show: false,
        },
    },
    colors: ['#5350e9', '#dc3545'],
    stroke: {
        width: 2,
    },
    grid: {
        show: false,
    },
    dataLabels: {
        enabled: false
    },
    xaxis: {
        type: 'datetime',
        categories: ["2018-09-19T00:00:00.000Z", "2018-09-19T01:30:00.000Z", "2018-09-19T02:30:00.000Z", "2018-09-19T03:30:00.000Z", "2018-09-19T04:30:00.000Z", "2018-09-19T05:30:00.000Z", "2018-09-19T06:30:00.000Z", "2018-09-19T07:30:00.000Z", "2018-09-19T08:30:00.000Z", "2018-09-19T09:30:00.000Z", "2018-09-19T10:30:00.000Z", "2018-09-19T11:30:00.000Z"],
        axisBorder: {
            show: false
        },
        axisTicks: {
            show: false
        },
        labels: {
            show: false,
        }
    },
    show: false,
    yaxis: {
        labels: {
            show: false,
        },
    },
    tooltip: {
        x: {
            format: 'dd/MM/yy HH:mm'
        },
    },
};

const renderDonut = async (result, id = '#chart-visitors-profile-test') => {
    let datas = result.series.map(e => {
        return e.datas[0];
    });
    let names = result.series.map(e => {
        return e.name;
    });

    let optionsDonut = {
        ...optionsDonutForm,
        series: datas,
        labels: names
    }
    $("#chart-visitors-profile-test").html('');
    let chartDonut = await new ApexCharts(document.querySelector(id), optionsDonut);
    chartDonut.render();
}

const renderBar = async (result) => {
    let datas = result.series[0].datas;
    let labels = result.xaxis.categories;
    let name = result.series[0].name;
    let optionsBar = {
        ...optionsBarForm,
        series: [{
            name: name,
            data: datas
        }],
        xaxis: {
            categories: labels,
        },
    }
    $("#chart-profile-visit-test").html('');
    let chartBar = await new ApexCharts(document.querySelector("#chart-profile-visit-test"), optionsBar);
    chartBar.render();
}


const renderArea = async (result) => {
    let series = result.series.map(e => {
        return {
            name: e.name,
            data: e.datas
        }
    });
    let labels = result.xaxis.categories;
    let optionsArea = {
        ...optionsAreaForm,
        series: series,
        xaxis: {
            categories: labels,
        },
    }
    $("#chart-area-two").html('');
    let chartArea = await new ApexCharts(document.querySelector("#chart-area-two"), optionsArea);
    chartArea.render();
}

function renderOverviewReport(result) {
    $('#overviewReport').show();
    $('#tableReport').hide();
    $('#totalMedical').html(result.totalMedical);
    $('#totalMedicalCancel').html(result.totalMedicalCancel);
    $('#totalMedicalComplete').html(result.totalMedicalComplete);
    $('#totalMedicalWait').html(result.totalMedicalWait);
    if (!!result.donut) {
        renderDonut(result.donut);
    }
    if (!!result.bar) {
        renderBar(result.bar);
    }
    if (!!result.area) {
        renderArea(result.area);
    }
}

function renderChart() {
    let form = {
        'typeReport': 1,
        'timeReport': 5,
    }
    let urlPath = window.location.origin;
    $.ajax({
        url: urlPath + "/api/sale-report",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(form),
        cache: false,
        success: function (result) {
            renderOverviewReport(result);
        },
        error: function (e) {
            console.log('Đã có lỗi xảy ra' + e);
        }
    });
}

window.onload = renderChart;
