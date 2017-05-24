class fragment start follow = object (self)
    val mutable starting: Position.position = start
    val mutable following: Position.position = follow

    method starting = starting
    method following = following

    method to_string =
        starting#to_string ^ "-" ^ following#to_string
    
    end;;
