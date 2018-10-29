class Skater(val roster: List<Person>)
class SkaterJersy(val roster: List<JerseyNumber>)
class SkaterPosition(val roster: List<Position>)

class Person(val person: FullName)
class JerseyNumber (val jerseyNumber: String)

class FullName(val fullName: String)

class Position(val position: Name)

class Name(val name: String)