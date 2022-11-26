**Under development.**

The Darts Scoring App allows you to set up a game, record each throw at the board and get back the actual state of the
game, including detailed statistics.

#### Set up the game

Send to `POST /new-game` the following as query params:

* `sets`: a positive odd integer of the maximum number of sets to be played. The game ends if one of the players is a
  sure winner.
* `legsPerSet`: a positive odd integer of the maximum number of legs to be played per set. The set ends early if one of
  the players is a sure winner.
* `initialScore`: the starting scores before the first throw in each leg, e.g. 501
* `gameStyle`: currently only the values `STANDARD` or `DOUBLES_OR_INNER_BULL_OUT` are accepted which mean the same
  thing, i.e. a leg can only be won with a double or inner bull's eye throw.
* `startingPlayer`: name of the starting player
* `opponent`: name of the opponent

Input validation is implemented to provide informative error messages.

In the response body, you will find the `gameId` which you will need to record the throws, as well as the confirmation
of the game setup.

#### Recording a dart throw

Each throw will be recorded via `POST /throw`. The two required query params are `gameId` and `value`.
The `value` param contains the details about the last throw in the following format:

* Single ring: `<sector-value>`, i.e. `1` through `20`
* Double ring: `d<sector-value>`
* Triple ring: `t<sector-value>`
* Outer bull's eye: `25`
* Inner bull's eye: `50`
* Miss: `x`

Uppercase is also allowed.

Informative error messages are shown if input is not compliant.

The response body contains the current state of the game, including statistics for both players, like so:
```json5
{
    "winner": null,
    "nextToThrow": "starting",
    "arrowLeftForNextPlayer": 3,
    "currentStanding": {
        "legs won set": {
            "opponent": 1,
            "starting": 1
        },
        "scores current leg": {
            "opponent": 501,
            "starting": 501
        },
        "sets won": {
            "opponent": 0,
            "starting": 0
        }
    },
    "gameStyle": {
        "sets": 3,
        "legsPerSet": 3,
        "initialScore": 501,
        "outshotStyle": "DOUBLE_OR_INNER_BULL_OUT"
    },
    "playerStatistics": [
        {
            "playerName": "opponent",
            "legsPlayed": 2,
            "legsWon": 1,
            "average": 136.125,
            "firstNineDartAverage": 138.8,
            "oneEighties": 0,
            "tonPluses": 4,
            "nineDarters": 0,
            "highestOut": 32,
            "tonPlusOuts": [],
            "outshotEfficiency": 100.0
        },
        {
            "playerName": "starting",
            "legsPlayed": 2,
            "legsWon": 1,
            "average": 149.5,
            "firstNineDartAverage": 149.5,
            "oneEighties": 2,
            "tonPluses": 5,
            "nineDarters": 1,
            "highestOut": 141,
            "tonPlusOuts": [
                141
            ],
            "outshotEfficiency": 100.0
        }
    ]
}
```

Currently, there is no option for modification of previous input values.

