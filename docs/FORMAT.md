The generated JSON data is an array of objects. Each object represents a shop sign and its metadata.
Inside each sign object is an array of item objects and their metadata.

# Sign object

Each shop sign is represented by an object, that _always_ contains the following properties:

* `invInStock` - Whether this sign is in stock or not, as a boolean. Behavior differs. . .
    * . . .if using **SignShop**:
        * `true` if `buy` or `ibuy` type and has stock available
        * `true` if `sell` or `isell` type and has free space available
        * `true` for all other types of sign, regardless of actual stock status
    * . . .if using **QuickShop**:
        * `true` if `buy` type and has stock available
        * `true` if `sell` type and has free space available
* `id` - Sign's unique ID number, calculated from world and position coordinates
* `locWorld` - Sign's world
* `locX` - Sign's integer X block position
* `locY` - Sign's integer Y block position
* `locZ` - Sign's integer Z block position
* `ownerName` - Sign owner's name (might not be up-to-date!)
* `ownerUuid` - Sign owner's UUID; use this for identification
* `signPrice` - Sign's price, as a positive decimal number
* `signText` - Array of the sign's 2nd and 3rd lines as strings. Empty lines are empty strings.
* `signType` - String denoting the sign's type (e.g. `buy` or `sell`). Depending on the plugin. . .
    * **SignShop** - Always lowercase; can be any SignShop type (e.g. `buy`, `ibuy`, `night`, etc.)
    * **QuickShop** - Only ever `buy` or `sell`

Finally, there is the `invItems` property. This is an array of item objects that this sign offers
(e.g. to buy or sell). Note however:

* `invItems` might be `undefined`, especially if it's not a `buy` or `sell` sign
* For **QuickShop**, this array will only ever have a maximum of one object

# Item object

Each shop's item is represented by an object, that _always_ contains the following properties:

* `type` - Item's classic Minecraft ID positive integer
* `durability` - Either the item's durability, or classic Minecraft data type integer
* `amount` - Amount of this item in the stack, as a positive integer

Optionally, these is a `meta` objec. If present, this contains extra data for that item. This data
varies, depending on the type of item. Native GSON serialization is not used, as some data is too
verbose or unnecessary for export.

Every property in the following meta sections, should be assumed optional (i.e. possibly undefined).

## Generic meta

Any item potentially have any of the following data:

* `displayName` - Custom name given to item using an anvil
* `lore` - Array of four strings; vanilla lore lines given to item (e.g. via `/give`)
* `unbreakable` - `true` if item is unbreakable

## Enchantment meta

Enchantable items may hold an `enchantments` object. Each key is the [Bukkit enchantment name][1],
and its associated value is the enchantment's level, as a positive integer.

## Banner meta

Banners potentially have any of the following data:

* `baseColor` - [Bukkit dye color][2] used as the banner's base
* `patternCount` - Positive integer count of how many patterns in the banner

## Book meta

Books (e.g. written, book and quill) potentially have any of the following data:

* `author` - String of book's author
* `title` - String of book's title
* `generation` - String of book's [Bukkit generation][3]
* `pageCount` - Positive integer count of how many pages in the book

## Firework meta

Fireworks potentially have any of the following data:

* `power` - Positive integer count of how many seconds this firework lasts
* `effectCount` - Positive integer count of this firework's effects

## Firework effect meta

Firework charges (holding a single effect) potentially have any of the following data:

* `flicker` - Boolean of whether or not this effect flickers
* `trail` - Boolean of whether or not this effect has a trail
* `type` - String of effect's [Bukkit type][4]

## Leather armor meta

Leather armor potentially have any of the following data:

* `colorR` - Integer between 0 and 255 of the armor color's red value
* `colorG` - Integer between 0 and 255 of the armor color's blue value
* `colorB` - Integer between 0 and 255 of the armor color's green value

## Map meta

Maps potentially have any of the following data:

* `locName` - String of map's custom location name
* `colorR` - Integer between 0 and 255 of the map icon color's red value
* `colorG` - Integer between 0 and 255 of the map icon color's blue value
* `colorB` - Integer between 0 and 255 of the map icon color's green value

## Potion meta

Potions potentially have any of the following data:

* `colorR` - Integer between 0 and 255 of potion's custom color red value
* `colorG` - Integer between 0 and 255 of potion's custom color blue value
* `colorB` - Integer between 0 and 255 of potion's custom color green value
* `customEffectCount` - Positive integer count of this potion's custom effects
* `extended` - Boolean of whether or not this potion's effect is extended (8 minutes)
* `upgraded` - Boolean of whether or not this potion's effect is doubled
* `type` - String of potion's [Bukkit type][5]

## Skull meta

Skulls potentially have an `owner` string property. This is the skull's owner.

## Spawn egg meta

Spawn eggs potentially have a `type` string property. This is the egg's [Bukkit type][6].

# Example

This example represents the data of a single sign, that sells two items: fireworks and potions.

```json
[
  {
    "locWorld": "world",
    "locX": 120,
    "locY": 64,
    "locZ": 128,
    "ownerName": "VanDerProtofsky",
    "ownerUuid": "b4b7d036-3d80-41d7-bee8-45ea6f5f21f3",
    "signText": [
      "Fireworks &",
      "Potions"
    ],
    "signType": "buy",
    "signPrice": 99.0,
    "invItems": [
      {
        "type": 402,
        "amount": 16,
        "durability": 0,
        "meta": {
          "displayName": "Creeper Firework",
          "lore": [
            "Scare your",
            "enemies from afar!",
            "",
            ""
          ],
          "flicker": true,
          "trail": true,
          "type": "CREEPER"
        }
      },
      {
        "type": 438,
        "amount": 1,
        "durability": 0,
        "meta": {
          "type": "NIGHT_VISION",
          "extended": true,
          "upgraded": false,
          "colorR": 1,
          "colorG": 106,
          "colorB": 169,
          "customEffectCount": 2
        }
      }
    ],
    "invInStock": true
  }
]
```

[1]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html
[2]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/DyeColor.html
[3]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/meta/BookMeta.Generation.html
[4]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/FireworkEffect.Type.html
[5]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionType.html
[6]: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html