$('#btn-add-item').on('click', function() {
    if ($('form')[0].getAttribute('action').indexOf('deleteInvoice') >= 0) {
        return;
    }
    var last = $('.item-record').last();
    var newNodes = $.parseHTML(
        '<div class="row mb-3 item-record">' +
        '   <div class="input-group col-md-11">' +
        '       <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" required>' +
        '       <input class="col-md-4 form-control" type="text" placeholder="Description" name="itemDesc[]" required>' +
        '       <input class="col-md-2 form-control item-count" type="number" min="1" placeholder="1" name="itemCount[]" required>' +
        '       <input class="col-md-2 form-control item-price" type="number" min="0.01" step="0.01" placeholder="0.01" name="itemPrice[]" required>' +
        '       <div class="col-md-1 no-padd input-group-append">' +
        '           <span class="input-group-text">&euro;</span>' +
        '       </div>' +
        '   </div>' +
        '   <div class="col-md-1">' +
        '       <span class="btn btn-danger btn-remove-item"><i class="fas fa-minus"></i></span>' +
        '   </div>' +
        '</div>');


    last.removeClass('mb-3');
    last.addClass('mb-2');
    $('#items-after').before($(newNodes));
});

$(document).on('click', '.btn-remove-item', function() {
    if ($('form').attr('action').indexOf('deleteInvoice') >= 0) {
        return;
    }
    if ($('.item-record').length > 1) {
        $(this).parent().parent().remove();
    }

    var last = $('.item-record').last();
    $(last).removeClass('mb-2').addClass('mb-3');
    recalculate();
});

$(document).on('change', '.item-count', function () {
    recalculate();
});

$(document).on('change', '.item-price', function () {
    recalculate();
});

function recalculate() {
    var counts = $('.item-count');
    var prices = $('.item-price');

    var total = 0;
    for (var i = 0; i < counts.length; i++) {
        var plus = Number(counts[i].value) * Number(prices[i].value);
        if (plus <= 0) {
            total = 'error';
            break;
        }
        total += plus;
    }
    $('#price').val(total);
}