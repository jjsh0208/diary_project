document.getElementById('file').addEventListener('change', function(event) {
    var fileInput = event.target;
    var fileName = fileInput.files[0].name;
    document.querySelector('.upload-name').value = fileName;
});