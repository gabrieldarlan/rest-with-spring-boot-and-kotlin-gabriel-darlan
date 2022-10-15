package br.com.gdarlan

import br.com.gdarlan.services.MathService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class MathController(private val mathService: MathService) {

    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"])
    fun sum(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?,
    ): Double {
        return mathService.sum(numberOne, numberTwo)
    }


    @RequestMapping(value = ["/minus/{numberOne}/{numberTwo}"])
    fun minus(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?,
    ): Double {
        return mathService.minus(numberOne, numberTwo)
    }


    @RequestMapping(value = ["/multi/{numberOne}/{numberTwo}"])
    fun multi(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?,
    ): Double {
        return mathService.multi(numberOne, numberTwo)
    }

    @RequestMapping(value = ["/div/{numberOne}/{numberTwo}"])
    fun div(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?,
    ): Double {
        return mathService.div(numberOne, numberTwo)
    }


    @RequestMapping(value = ["/pow/{numberOne}/{numberTwo}"])
    fun pow(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?,
    ): Double {
        return mathService.pow(numberOne, numberTwo)
    }


    @RequestMapping(value = ["/sqrt/{numberOne}"])
    fun sqrt(
        @PathVariable(value = "numberOne") numberOne: String?,
    ): Double {
        return mathService.raiz(numberOne)
    }


}