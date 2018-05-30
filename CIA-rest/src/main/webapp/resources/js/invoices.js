var inputListType = $('#listType');
var inputListOldest = $('#listOldest');
var inputListNewest = $('#listNewest');
var inputListUser = $('#listPerson');

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
    })
});

$('#submit-edit').on('click', function() {
    $('.details-form form').attr('action', '/accounting/editInvoice?id=' + $('#id').val()).submit();
});

$('#submit-delete').on('click', function() {
    $('.details-form form').attr('action', '/accounting/deleteInvoice?id=' + $('#id').val()).submit();
});

function hideInput(container) {
    container.addClass('input-hdn');
    $.each($(container).find('input'), function () {
        $(this).prop('required', false);
        $(this).prop('disabled', true);
    });
    $.each($(container).find('select'), function () {
        $(this).prop('required', false);
        $(this).prop('disabled', true);
    });
}

function showInput(container) {
    container.removeClass('input-hdn');
    $.each($(container).find('input'), function () {
        $(this).prop('required', true);
        $(this).prop('disabled', false);
    });
    $.each($(container).find('select'), function () {
        $(this).prop('required', true);
        $(this).prop('disabled', false);
    });
}

inputListType.change(function() {
    var val = $(this).val();
    switch (val) {
        case 'dates':
            showInput(inputListOldest);
            showInput(inputListNewest);
            hideInput(inputListType);
            hideInput(inputListUser);
            break;
        case 'type':
            hideInput(inputListOldest);
            hideInput(inputListNewest);
            showInput(inputListType);
            hideInput(inputListUser);
            break;
        case 'user':
            hideInput(inputListOldest);
            hideInput(inputListNewest);
            hideInput(inputListType);
            showInput(inputListUser);
            break;
        case 'userAndDate':
            showInput(inputListOldest);
            showInput(inputListNewest);
            hideInput(inputListType);
            showInput(inputListUser);
            break;
        case 'typeAndDate':
            showInput(inputListOldest);
            showInput(inputListNewest);
            showInput(inputListType);
            hideInput(inputListUser);
            break;
        case 'userAndType':
            showInput(inputListOldest);
            showInput(inputListNewest);
            showInput(inputListType);
            showInput(inputListUser);
            break;
        default: {
            hideInput(inputListOldest);
            hideInput(inputListNewest);
            hideInput(inputListType);
            hideInput(inputListUser);
        }
    }
});

inputListOldest.change(function() {
    inputListNewest.min = inputListOldest.val();
});

$('#list-type').val('all').change();