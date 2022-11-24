**In development.**

The Darts Scoring App will allow you to set up a game, record each throw at the board and get back the actual state of the game.

#### Set up the game
The following needs to be provided:
* at what score the counting starts, e.g. 501
* game style (currently Doubles/Inner Bull's Eye out is implemented)
* the value of X in: best of X sets
* the value of Y in: best of Y legs per set
* name of starting player
* name of opponent

#### Recording a dart throw
Each throw will be sent as string to the API. Formats:
* Single ring: `<sector-value>`, i.e. `1` through `20`
* Double ring: `d<sector-value>` 
* Triple ring: `t<sector-value>` 
* Outer bull's eye: `25`
* Inner bull's eye: `50`
* Miss: `x`

(TODO: Parsing from string will be refactored.)

Uppercase is also allowed.

Informative error messages are shown if input is not compliant.

Currently no option for modification of previous input values.

