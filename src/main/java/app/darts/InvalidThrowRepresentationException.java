package app.darts;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class InvalidThrowRepresentationException extends RuntimeException {

    private static final String FORMATTING_GUIDANCE_URL = linkTo(methodOn(DartsController.class).sendFormatHelp())
            .withSelfRel().getHref();

    public InvalidThrowRepresentationException(String reason, String input) {
        super(reason + (reason.endsWith(".") ? "" : ".") +  " Input: " + input
                + ". Read more about formatting rules at " + FORMATTING_GUIDANCE_URL);
    }
}
