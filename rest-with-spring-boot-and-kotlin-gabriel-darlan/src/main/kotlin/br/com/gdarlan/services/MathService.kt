package br.com.gdarlan.services

import br.com.gdarlan.exceptions.UnsurppotedMathOperationException
import br.com.gdarlan.services.utils.MathUtil
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class MathService(
    private val mathUtil: MathUtil,
) {

    fun sum(numberOne: String?, numberTwo: String?): Double {
        if (!mathUtil.isNumeric(numberOne) || !mathUtil.isNumeric(numberTwo))
            throw UnsurppotedMathOperationException("Please set a numeric value.")
        return mathUtil.convertToDouble(numberOne) + mathUtil.convertToDouble(numberTwo)
    }

    fun minus(numberOne: String?, numberTwo: String?): Double {
        if (!mathUtil.isNumeric(numberOne) || !mathUtil.isNumeric(numberTwo))
            throw UnsurppotedMathOperationException("Please set a numeric value.")
        return mathUtil.convertToDouble(numberOne) - mathUtil.convertToDouble(numberTwo)
    }

    fun multi(numberOne: String?, numberTwo: String?): Double {
        if (!mathUtil.isNumeric(numberOne) || !mathUtil.isNumeric(numberOne))
            throw UnsurppotedMathOperationException("Please set a numeric value.")
        return mathUtil.convertToDouble(numberOne) * mathUtil.convertToDouble(numberTwo)
    }

    fun div(numberOne: String?, numberTwo: String?): Double {
        if (!mathUtil.isNumeric(numberOne) || !mathUtil.isNumeric(numberTwo))
            throw UnsurppotedMathOperationException("Please set a numeric value.")

        val valor =
            if (mathUtil.convertToDouble(numberOne).equals(0.0) || mathUtil.convertToDouble(numberTwo).equals(0.0)) {
                throw UnsurppotedMathOperationException("A divisão por zero não é definida")
            } else {
                mathUtil.convertToDouble(numberOne) / mathUtil.convertToDouble(numberTwo)
            }
        return valor
    }

    fun pow(numberOne: String?, numberTwo: String?): Double {
        if (!mathUtil.isNumeric(numberOne) || !mathUtil.isNumeric(numberTwo))
            throw UnsurppotedMathOperationException("Please set a numeric value.")

        val valor =
            if (mathUtil.convertToDouble(numberOne).equals(0.0) || mathUtil.convertToDouble(numberTwo).equals(0.0)) {
                throw UnsurppotedMathOperationException("A potencia por zero não é definida")
            } else {
                mathUtil.convertToDouble(numberOne).pow(mathUtil.convertToDouble(numberTwo))
            }
        return valor
    }

    fun raiz(numberOne: String?): Double {
        if (!mathUtil.isNumeric((numberOne)))
            throw UnsurppotedMathOperationException("Please set a numeric value.")

        val valor = if (mathUtil.convertToDouble(numberOne).equals(0.0)) {
            throw UnsurppotedMathOperationException("A raiz quadrada por zero não é definida")
        } else {
            kotlin.math.sqrt(mathUtil.convertToDouble(numberOne))
        }
        return valor
    }

}