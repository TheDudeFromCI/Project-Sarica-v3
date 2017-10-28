## General Intelligence

### 10/28/17

General Intelligence is simply a brute force search with previously aquired knowledge as a probability filter. This may sound a bit odd, but bare with me. General Intellgence, as I defined on the README for this project repository, has three criteria that must be met. First, an agent must have the ability to aquire all available knowlwdge that can be obtained within its own environment (Though not nessicarily all at once). Second it must be able to apply any aquired knowledge in order to solve a problem. Lastly, it must be able to create its own subgoals and move towards complelting them.

While it may not seem like this approach fulfills those categories, it does a lot more then one might realize. This is easist to vizualize in a simple world.

-----
Example, Tic Tac Toe:
Imagine a universe where the entire environment is just Tic Tac Toe. Theres no such thing as night and day, sleep, emotion, etc. Just the game, and positive/negative feedback. There are two laws of physics in this world. First, when it is your turn, you must place a marker down. No matter how long it takes. Number two, you cannot place a marker on an already occupied tile. You are given the goal of winning, with no further knowledge.

You start off not knowing anything about what "win" means. You are an infant at this point. It is your turn. Because there is not search space goal (note: in this example context, the search space is all possible actions), you preform an action randomly. You place your marker down on the top left tile. You now see there is an "X" in the top left tile. The opponent makes a move. You now see a "O" in the center tile. You still do not know the search space, so you make a move randomly. You place a marker on the center tile. You suddenly have a negative feedback and it is your turn again.You have thus aquired some knowledge that it is possible placing in the center tile is bad, or placing on an "O" is bad, or placing a marker on another marker is bad. All of these oberservations are recorded. (Note: Other oberservations could also be made. Each oberservation is a simple cause and effect pattern found. More advanced agents may search for larger and more complex patterns, such as number of moves, order of placement, current goal at the time of the feedback, etc) For limited processing power in massive pattern space, many obervations are made at random.

You now have a search space, though you don't really know what you're searching for. You begin thinking. Your mind begins searching through all possible actions, with negative bias towards placing a marker in the center, on the "O", or on another tile. You place the next marker on the the bottom left.

This back and forth motion continues until the final move. You place your piece in the bottom left corner. The opponent makes a move. You feel a sudden massive negative feedback, and the board is reset. You are given the goal to win the game. More observations on the state of the world are made, such as really don't pick the bottom left corner, and don't let "O"s be placed on certain tiles.

After many, many iterations, the agent begins to gain an understanding of the concept of the game and plans it's moves out better based on this knowledge probability biases.
-----

While that example is extremely long winded, I hope it makes a good point. For larger and more complex environments, the effect of this increases signifigantly. In addition, for massivly complex environments, the entire search space, (all possible actions) does not need to be searched. Only potentially benificial actions. This search system is ran for the goal the agent is currently pursuing. For more complex goals, goals can be broken down into mutitple smaller goals based on the same search space technique. Plus, this search space method is recursive. Each major action, actions for completing that action, fine details, etc.

It is also important to note, it is mentioned above that the patterns an agent find make a large difference in the growth of the agent. An agent that is unable to see complex patterns is highly hindered in its growth. For this reason, it is also very important for an agent to also gain the ability to apply aquired knowledge for uipgrading its pattern finding abilities. This should be simple, as neural networks handle pattern recognition fairly well.
