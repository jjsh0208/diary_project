document.getElementById('toggleButton').addEventListener('click', function() {
    var menu = document.getElementById('menu');
    if (menu.style.left === '-400px') {
        menu.style.left = '0';
    } else {
        menu.style.left = '-400px';
    }
    event.stopPropagation(); // 이벤트 전파 중지
});

document.getElementById('closeButton').addEventListener('click', function() {
    var menu = document.getElementById('menu');
    menu.style.left = '-400px';
});