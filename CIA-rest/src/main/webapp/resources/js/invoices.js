var inputListType = $('#listType');
var inputListOldest = $('#listOldest');
var inputListNewest = $('#listNewest');
var inputListUser = $('#listPerson');

$('.more').click(function () {
    $('#details').css('display', 'block');
    var row = $(this).parent()[0];
    var id = row.getElementsByClassName('id')[0].textContent;
    var secondPerson = row.getElementsByClassName('secondPerson')[0].textContent;
    var issued = row.getElementsByClassName('issued')[0].textContent;
    var dueTo = row.getElementsByClassName('due-to')[0].textContent;
    var price = row.getElementsByClassName('price')[0].textContent;
    var type = row.getElementsByClassName('type')[0].textContent;
    var items = row.getElementsByClassName('item');

    $('#details-header')[0].textContent = 'Invoice #' + id;
    $('#id')[0].setAttribute('value', id);
    $('#secondPerson')[0].setAttribute('value', secondPerson);
    $('#issued')[0].setAttribute('value', issued);
    $('#dueTo')[0].setAttribute('value', dueTo);
    $('#price')[0].setAttribute('value', price);
    $('#type')[0].setAttribute('value', type);

    for (var i=0; i < items.length; i++) {
        var name = items[i].getElementsByClassName('item-name')[0].textContent;
        var desc = items[i].getElementsByClassName('item-desc')[0].textContent;
        var count = items[i].getElementsByClassName('item-count')[0].textContent;
        var itemPrice = items[i].getElementsByClassName('item-price')[0].textContent;
        var newNodes = $.parseHTML(
            '<div class="row mb-3 item-record">' +
            '   <div class="input-group col-md-11">' +
            '       <input class="col-md-3 form-control" type="text" name="itemName[]" value="' + name + '" disabled>' +
            '       <input class="col-md-4 form-control" type="text" name="itemDesc[]" value="' + desc + '" disabled>' +
            '       <input class="col-md-2 form-control item-count" type="number" min="1" placeholder="1" type="text" name="itemCount[]" value="' + count + '" disabled>' +
            '       <input class="col-md-2 form-control item-price" type="number" step=".01" min="0.01" placeholder="0.01" type="text" name="itemPrice[]" value="' + itemPrice + '" disabled>' +
            '       <div class="col-md-1 no-padd input-group-append">' +
            '           <span class="input-group-text">&euro;</span>' +
            '       </div>' +
            '   </div>' +
            '</div>');
        $('#items-after')[0].before(newNodes[0]);
    }
});

$('#details-close').on('click', function() {
    $('#details').css('display', 'none');
    $.each($('.item-record'), function () {
        this.remove();
    })
});

$('#submit-edit').on('click', function() {
    var id = $('#details #id')[0].getAttribute('value');
    $('.details-form')[0].setAttribute('action', '/accounting/editInvoice?id=' + id);
    $('.details-form')[0].submit();
});

$('#submit-delete').on('click', function() {
    var id = $('#details #id')[0].getAttribute('value');
    $('.details-form')[0].setAttribute('action', '/accounting/deleteInvoice?id=' + id);
    $('.details-form')[0].submit();
});

function hideInput(container) {
    container.addClass('input-hdn');
    $.each(container[0].getElementsByTagName('input'), function () {
        this.required = false;
        this.disabled = true;
    });
    $.each(container[0].getElementsByTagName('select'), function () {
        this.required = false;
        this.disabled = true;
    });
}

function showInput(container) {
    container.removeClass('input-hdn');
    $.each(container[0].getElementsByTagName('input'), function () {
        this.required = true;
        this.disabled = false;
    });
    $.each(container[0].getElementsByTagName('select'), function () {
        this.required = true;
        this.disabled = false;
    });
}

$('#list-type').change(function() {
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