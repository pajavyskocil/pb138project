$('.more').click(function () {
    $('#details').css('display', 'block');
    var row = $(this).parent()[0];
    var id = row.getElementsByClassName('id')[0].textContent;
    var name = row.getElementsByClassName('name')[0].textContent;
    var email = row.getElementsByClassName('email')[0].textContent;
    var phone = row.getElementsByClassName('phone')[0].textContent;
    var accNr = row.getElementsByClassName('accNr')[0].textContent;
    var street = row.getElementsByClassName('street')[0].textContent;
    var city = row.getElementsByClassName('city')[0].textContent;
    var zip = row.getElementsByClassName('zip')[0].textContent;
    var country = row.getElementsByClassName('country')[0].textContent;

    $('#details-header')[0].textContent = name;
    $('#id')[0].setAttribute('value', id);
    $('#name')[0].setAttribute('value', name);
    $('#email')[0].setAttribute('value', email);
    $('#phone')[0].setAttribute('value', phone);
    $('#accNr')[0].setAttribute('value', accNr);
    $('#street')[0].setAttribute('value', street);
    $('#city')[0].setAttribute('value', city);
    $('#zip')[0].setAttribute('value', zip);
    $('#country')[0].setAttribute('value', country);
});

$('#details-close').on('click', function() {
    $('#details').css('display', 'none');
});

$('#submit-edit').on('click', function() {
    var id = $('#details #id')[0].getAttribute('value');
    $('.details-form')[0].setAttribute('action', '/accounting/editPerson?id=' + id);
    $('.details-form')[0].submit();
});

$('#submit-delete').on('click', function() {
    var id = $('#details #id')[0].getAttribute('value');
    $('.details-form')[0].setAttribute('action', '/accounting/deletePerson?id=' + id);
    $('.details-form')[0].submit();
});

$('#filter').on('keyup', function() {
    var val = $(this).val();

    $.each($('.record'), function() {
        var tds = $(this)[0].getElementsByTagName('td');
        var visible = false;
        for (var i=1; i < 3; i++) {
            var content = tds[i].textContent;
            if (content.toLowerCase().indexOf(val.toLowerCase()) >= 0) {
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