function urbixAbrirFoto(ruta) {
    var visor = document.getElementById('urbix-visor');
    var imagen = document.getElementById('urbix-visor-imagen');
    imagen.src = ruta;
    visor.classList.add('visible');
}

function urbixCerrarFoto() {
    document.getElementById('urbix-visor').classList.remove('visible');
    document.getElementById('urbix-visor-imagen').src = '';
}

document.addEventListener('DOMContentLoaded', function () {
    var visor = document.getElementById('urbix-visor');
    if (visor) {
        visor.addEventListener('click', function (evento) {
            if (evento.target === visor) {
                urbixCerrarFoto();
            }
        });
    }
    document.addEventListener('keydown', function (evento) {
        if (evento.key === 'Escape') {
            urbixCerrarFoto();
        }
    });
});