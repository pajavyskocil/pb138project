$('.more').click(function () {
    $('#details').css('display', 'block');
    var row = $(this).parent();

    $('#details-header').text(row.find('.name').text());
    $('#id').val(row.find('.id').text());
    $('#name').text(row.find('.name').text());
    $('#email').text(row.find('.email').text());
    $('#phone').text(row.find('.phone').text());
    $('#accNr').text(row.find('.accNr').text());
    $('#street').text(row.find('.street').text());
    $('#city').text(row.find('.city').text());
    $('#zip').text(row.find('.zip').text());
    $('#country').text(row.find('.country').text());
});

$('#details-close').on('click', function() {
    $('#details').css('display', 'none');
});

$('#submit-edit').on('click', function() {
    $('.details-form form').attr('action', '/accounting/editPerson?id=' + $('#id').val()).submit();
});

$('#submit-delete').on('click', function() {
    $('.details-form form').attr('action', '/accounting/deletePerson?id=' + $('#id').val()).submit();
});

$('#filter').on('keyup', function() {
    var val = $(this).val();

    $.each($('.record'), function() {
        var tds = $(this).find('td');
        var visible = false;
        for (var i=1; i < 3; i++) {
            if ($(tds[i]).text().toLowerCase().indexOf(val.toLowerCase()) >= 0) {
                visible = true;
                break;
            }
        }

        if (visible) {
            $(this).css('visibility', 'visible');
        } else {
            $(this).css('visibility', 'collapse');
        }
    });
});