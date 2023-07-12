function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/user/logout',
        'method': 'post',
    })
}

function getCode(params) {
    return $axios({
        'url': '/user/code',
        'method': 'get',
        params
    })
}
  