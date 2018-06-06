var inputListType = $('#filter-type');
var inputListOldest = $('#filter-oldest');
var inputListNewest = $('#filter-newest');
var inputListUser = $('#filter-personId');
var inputFrom = $('#from');
var inputTo = $('#to');

$('.more').click(function () {
    $('#details').css('display', 'block');
    var row = $(this).parent();
    var items = row.find('.item');

    $('#details-header').text('Invoice #' + row.find('.id').text());
    $('#id').val(row.find('.id').text());
    $('#secondPerson').text(row.find('.secondPerson').text());
    $('#issued').text(row.find('.issued').text());
    $('#dueTo').text(row.find('.due-to').text());
    $('#price').text(row.find('.price').text());
    $('#type').text(row.find('.type').text());

    for (var i=0; i < items.length; i++) {
        var name = $(items[i]).find('.item-name').text();
        var desc = $(items[i]).find('.item-desc').text();
        var count = $(items[i]).find('.item-count').text();
        var itemPrice = $(items[i]).find('.item-price').text();
        var newNodes = $.parseHTML(
            '<div class="row mb-3 item-record">' +
            '   <div class="input-group col-md-11">' +
            '       <div class="col-md-3 form-control">' + name + '</div>' +
            '       <div class="col-md-4 form-control">' + desc + '</div>' +
            '       <div class="col-md-2 form-control">' + count + '</div>' +
            '       <div class="col-md-2 form-control item-price">' + itemPrice + '</div>' +
            '       <div class="col-md-1 no-padd input-group-append">' +
            '           <span class="input-group-text">&euro;</span>' +
            '       </div>' +
            '   </div>' +
            '</div>');
        $('#items-after').before($(newNodes));
    }
});

$('#details-close').on('click', function() {
    $('#details').css('display', 'none');
    $.each($('.item-record'), function () {
        this.remove();
    });
});

$('#submit-edit').on('click', function() {
    $('.details-form form').attr('action', '/accounting/editInvoice?id=' + $('#id').val()).submit();
});

$('#submit-delete').on('click', function() {
    $('.details-form form').attr('action', '/accounting/deleteInvoice?id=' + $('#id').val()).submit();
});

$('#submit-pdf').on('click', function() {
    $('.details-form form').attr('action', '/accounting/exportInvoice?id=' + $('#id').val()).submit();
});

$('#export-invoices').on('click', function() {
    $('#export').css('display', 'block');
});

$('#export-close').on('click', function() {
    $('#export').css('display', 'none');
});

function disableInput(input) {
    $(input).prop('required', false);
    $(input).prop('disabled', true);
}

function enableInput(input) {
    $(input).prop('required', true);
    $(input).prop('disabled', false);
}

inputListType.change(function() {
    var val = $(this).val();
    switch (val) {
        case 'date':
            enableInput(inputListOldest);
            enableInput(inputListNewest);
            disableInput(inputListUser);
            break;
        case 'user':
            disableInput(inputListOldest);
            disableInput(inputListNewest);
            enableInput(inputListUser);
            break;
        case 'userDate':
            enableInput(inputListOldest);
            enableInput(inputListNewest);
            enableInput(inputListUser);
            break;
        default: {
            disableInput(inputListOldest);
            disableInput(inputListNewest);
            disableInput(inputListUser);
        }
    }
});

inputListOldest.change(function() {
    inputListNewest.prop('min', inputListOldest.val());
});

inputListNewest.change(function() {
    inputListOldest.prop('max', inputListNewest.val());
});

inputFrom.change(function() {
    inputTo.prop('min', inputFrom.val());
});

inputTo.change(function() {
    inputFrom.prop('max', inputTo.val());
});

inputListType.val('all').change();

$(".table-hider").on('click', function () {
    var i = $(this).find('i');
    if (i.hasClass('fa-angle-right')) {
        i.removeClass('fa-angle-right');
        i.addClass('fa-angle-down');
    } else {
        i.removeClass('fa-angle-down');
        i.addClass('fa-angle-right');
    }
});