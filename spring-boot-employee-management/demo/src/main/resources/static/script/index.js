$(document).on("click", ".del-modal", function () {
    var id = $(this).attr('data-id');
    var fn = $(this).attr('data-fullName');

    var title = document.getElementById('delModalLabel')
    title.innerHTML = "Delete " + fn + " ?"
   console.log(id , fn)
   var a = document.getElementById('delBtn')
   a.href = '/deleteEmployee/'+id
});


