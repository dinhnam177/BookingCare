$('.content__slider-img').slick({
    slidesToShow: 4,
    slidesToScroll: 1,
    // autoplay: true,
    autoplaySpeed: 1000,
    arrows: false,
    fade: false,
    // draggable: false, // khong cho  lay chuot di chuyen 
    isfinite: true,
    responsive: [{
        breakpoint: 740,
        settings: {
            slidesToShow: 1,

        }
    },
    {
        breakpoint: 1024,
        settings: {
            slidesToShow: 3,
        }
    },
    ]
});

$("#test").hover(function(){
	$(".update__Thongtin").css("display", "block");
}, function(){
    $(".update__Thongtin").css("display", "none");
});
$(".update__Thongtin").hover(function(){
	$(".update__Thongtin").css("display", "block");
}, function(){
    $(".update__Thongtin").css("display", "none");
});
//  slider
//code js 
const srcImg = document.querySelector('.js-slider')
// console.log([srcImg])
let index = 1;
changeimg = () => {
    var imgs = ["./assets/img/img7.jpg",
        "./assets/img/img9.jpg",
        "./assets/img/img10.jpg"
    ]
    srcImg.stlye.background = imgs[index]
    index++
    if (index == 3) {
        index = 0;
    }
}
// setInterval(changeimg, 1000)

//  cconsep chugn cua content  là nut arow
$('.content__list-video').slick({
    slidesToShow: 4,
    slidesToScroll: 1,
    arrows: true,
    isfinite: false,
    draggable: false,
    cssEase: 'linear',
    prevArrow: "<button type='button' class='slick-prev pull-left'><i class='fa fa-angle-left' aria-hidden='true'></i></button>",
    nextArrow: "<button type='button' class='slick-next pull-right'><i class='fa fa-angle-right' aria-hidden='true'></i></button>",
    responsive: [{
        breakpoint: 740,
        settings: {
            slidesToShow: 1,

        }
    },
    {
        breakpoint: 1024,
        settings: {
            slidesToShow: 3,
        }
    },
    ]
});

// content4 => handbook:Cẩm Nang
$('.content__list-handbook').slick({
    slidesToShow: 2,
    slidesToScroll: 1,
    arrows: true,
    isfinite: false,
    draggable: false,
    cssEase: 'linear',
    prevArrow: "<button type='button' class='slick-prev pull-left'><i class='fa fa-angle-left' aria-hidden='true'></i></button>",
    nextArrow: "<button type='button' class='slick-next pull-right'><i class='fa fa-angle-right' aria-hidden='true'></i></button>",
    responsive: [{
        breakpoint: 740,
        settings: {
            slidesToShow: 1,

        }
    },
    {
        breakpoint: 1024,
        settings: {
            slidesToShow: 2,
        }
    },
    ]
});

// // js đóng mở form 
// let modal = document.querySelector('.js-modal')
// let modal_1 = document.querySelector('.modal-1')
// let modalClose = document.querySelector('.js-icon-close')
// let modalClose_1 = document.querySelector('.js-icon-close_1')
// let jsdangNhap = document.querySelector('.js-dangNhap')
// let jsdangKy = document.querySelector('.js-dangKy')
// let modalContainer = document.querySelector('.modal-container')
// let modalContainer_1 = document.querySelector('.modal-container_1')
// let BtnSubmit = document.querySelector('.form-submit')
// let loginModal = document.querySelector('.heading-login')


// //  ấn vào btn đăng nhập  đăng ký trên thanh header
// jsdangNhap.addEventListener('click', () => {
//     modal.classList.toggle('active')
// })
// jsdangKy.addEventListener('click', () => {
//     modal_1.classList.toggle('active')

// })
// //  ngăn chặn hành vi nổi bọt
// modalContainer.addEventListener('click', (e) => {
//     e.stopPropagation()
// })

// modalContainer_1.addEventListener('click', (e) => {
//     e.stopPropagation()
// })
// //  ấn nút close thì đóng modal
// modalClose.addEventListener('click', () => {
//     modal.classList.remove('active')
// })
// modalClose_1.addEventListener('click', () => {
//     modal_1.classList.remove('active')
// })

// // ấn vô modal thì đóng form lại 
// modal.addEventListener('click', () => {
//     modal.classList.remove('active')
// })
// modal_1.addEventListener('click', () => {
//     modal_1.classList.remove('active')
// })
// BtnSubmit.addEventListener('click', (e) => {
//     e.preventDefault()
// })



let iconplus = document.querySelector('.icons-lpus')
let input_file = document.querySelector('.container-ipfile')
iconplus.addEventListener('click', () => {
    input_file.classList.toggle('active')
})

// modal-close

let modalclose = document.querySelector('.modal-close')
let btnCamera = document.querySelector('.icons-camera')
let modalcall = document.querySelector('.modal-overlay')
console.log(modalcall)

btnCamera.addEventListener('click', () => {
    modalcall.classList.add('openn')
})
modalclose.addEventListener('click', () => {
    modalcall.classList.remove('openn')
})

// modal bar 
let iconBar = document.querySelector('.bar')
let modalBar = document.querySelector('.modal-bar')
let closebar = document.querySelector('.close-bar')

iconBar.addEventListener('click', () => {
    modalBar.classList.add('show')
})
closebar.addEventListener('click', () => {
    modalBar.classList.remove('show')
})