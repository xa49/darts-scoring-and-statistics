package app.darts;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GameStyleValidatorDto implements ConstraintValidator<ValidGameStyleDto, GameStyleDto> {

    private GameStyleDto gameStyleDto;
    private ConstraintValidatorContext cvc;

    @Override
    public boolean isValid(GameStyleDto gameStyleDto, ConstraintValidatorContext cvc) {
        this.gameStyleDto = gameStyleDto;
        this.cvc = cvc;
        boolean validSetCount = isSetCountValid();
        boolean validLegCount = isLegCountValid();
        boolean validInitialScore = isInitialScoreValid();
        boolean validOutshotStyle = isOutshotStyleValid();
        return validSetCount && validLegCount && validInitialScore && validOutshotStyle;
    }

    private boolean isSetCountValid() {
        if (gameStyleDto.getSets() < 1 || gameStyleDto.getSets() % 2 == 0) {
            cvc.buildConstraintViolationWithTemplate("Total number of sets must be a positive, odd number.")
                    .addPropertyNode("sets").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isLegCountValid() {
        if (gameStyleDto.getLegsPerSet() < 1 || gameStyleDto.getLegsPerSet( ) % 2 == 0) {
            cvc.buildConstraintViolationWithTemplate("Legs per set must be a positive, odd number.")
                    .addPropertyNode("legsPerSet").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isInitialScoreValid() {
        if (gameStyleDto.getInitialScore() < DartsConstants.MINIMUM_CHECKOUT_VALUE) {
            cvc.buildConstraintViolationWithTemplate("Initial score must not be lower than "
                            + DartsConstants.MINIMUM_CHECKOUT_VALUE + ".")
                    .addPropertyNode("initialScore").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isOutshotStyleValid() {
        if(!DartsConstants.GAME_STYLES.containsKey(gameStyleDto.getOutshotStyle().toUpperCase())) {
            cvc.buildConstraintViolationWithTemplate("You must choose an implemented outshot style. Options: "
                    + DartsConstants.GAME_STYLES.keySet()).addPropertyNode("outshotStyle").addConstraintViolation();
            return false;
        }
        return true;
    }
}
