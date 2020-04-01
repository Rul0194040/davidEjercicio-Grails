package api.category

class UrlMappings {

    static mappings = {
        get "/$controller(.$format)?"(action:"index")
        patch "/$controller/$id(.$format)?"(action:"patch")
        post "/$controller(.$format)?"(action:"crear")
        get "/$controller/$id(.$format)?"(action:"leer")
        put "/$controller/$id(.$format)?"(action:"actualizar")
        delete "/$controller/$id(.$format)?"(action:"borrar")

        post "/$controller/tarifa(.$format)?"(action:"booking")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
