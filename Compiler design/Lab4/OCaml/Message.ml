open Batteries;;

class message is_error_ text_ position_ = object (self)
    
    val is_error: bool = is_error_
    val text: BatUTF8.t = text_
    val position: Position.position = position_

    method get_error = is_error
    method get_text = text
    method get_position = position

    end;;
