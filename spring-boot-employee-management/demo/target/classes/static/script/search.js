$(document).on("click", ".search-modal", function () {
    var id = $(this).attr('data-searchId');
    var fn = $(this).attr('data-searchFullName');

    var title = document.getElementById('searchModalLabel')
    title.innerHTML = "Delete " + fn + " ?"
   console.log(id , fn)
   var a = document.getElementById('delBtn')
   a.href = '/deleteEmployee/'+id
});


