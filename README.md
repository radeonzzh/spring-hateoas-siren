# Spring HATEOAS Siren
This module extends [Spring HATEOAS][] with the custom media type [Siren][]. 
> _Siren: a hypermedia specification for representing entities_

The media type of [Siren][] is defined as `application/vnd.siren+json`.

## Configuration
To enable the [Siren][] hypermedia type you simply need to add this module as a dependency to your project.

* Maven

```
<dependency>
    <groupId>com.github.ingogriebsch</groupId>
    <artifactId>spring-hateoas-siren</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

* Gradle

```
dependencies {
    compile "com.github.ingogriebsch:spring-hateoas-siren:1.0.0"
}
```

Having the module on the classpath of your project is all you need to do to get the hypermedia type automatically enabled. This way incoming requests asking for the mentioned media type will get appropriate responses.

## Behavior
Using this module will make your application respond to requests that have an `Accept` header of `application/vnd.siren+json` in the following way. 

In general each [Representation Model][Spring HATEOAS Representation Model] is rendered into a Siren [Entity][Siren Entity]. Depending on the type of respective type of the [Representation Model][Spring HATEOAS Representation Model] the following rules apply:

### RepresentationModel
When this module renders a `RepresentationModel`, it will

* map links of the representation model to Siren links and actions (see below to understand how links are rendered).
* map any custom properties of the representation model (because it was sub classed) to Siren properties.

### EntityModel
When this module renders an `EntityModel`, it will

* map links of the entity model to Siren links and actions (see below to understand how links are rendered).
* map the value of the content property of the entity model to Siren properties.

When this module renders an `EntityModel`, it will NOT

* map the value of the content property if the value is an instance of one of the available representation models!
* map custom properties of the entity model (if it has some because it was sub classed).

### CollectionModel
When this module renders a `CollectionModel`, it will

* map links of the collection model to Siren links and actions (see below to understand how links are rendered).
* map the size of the content property of the collection model to Siren properties.
* map the value of the content property of the collection model to Siren entities.

When this module renders a `CollectionModel`, it will NOT

* map custom properties of the collection model (if it has some because it was sub classed).

### PagedModel
When this module renders a `PagedModel`, it will

* map links of the paged model to Siren links and actions (see below to understand how links are rendered).
* map the page metadata of the paged model to Siren properties.
* map the value of the content property of the paged model to Siren entities.

When this module renders a `PagedModel`, it will NOT

* map custom properties of the paged model (if it has some because it was sub classed).

### Link
When this module renders a `Link`, it will

* map links to Siren links and it's corresponding affordances to Siren actions.

When this module renders a `Link`, it will NOT


## Restrictions
* Siren [Embedded Link][Siren Entity Embedded Link]s are currently not implemented through the module itself. If you want them, you need to implement a pojo representing an embedded link and add it as content of either a `CollectionModel` or `PagedModel` instance.
* Siren [Embedded Representation][Siren Entity Embedded Representation]s are currently only supported if defined as the content of either a `CollectionModel` or a `PagedModel` instance. It is currently not possible to build a hierarchy based on instances of either `RepresentationModel` or `EntityModel`.

## Customization
This module currently uses a really simple approach to map the respective model to the [class attribute][Siren Entity Class] of the [Siren Entity][]. If you want to override/enhance this behavior you need to expose an implementation of the `SirenEntityClassProvider` interface as a Spring bean.

## Client-side Support

### Deserialization
This module also allows to use/handle the [Siren][] hypermedia type on clients requesting data from servers producing this hypermedia type. Means that adding and enabling this module is sufficient to be able to deserialize responses containing data of the [Siren][] hypermedia type.

### Traverson
The hypermedia type `application/vnd.siren+json` is currently not usable with the `Traverson` implementation provided through [Spring HATEOAS][].

### Using LinkDiscoverer Instances
When working with hypermedia enabled representations, a common task is to find a link with a particular relation type in it. [Spring HATEOAS][] provides [JSONPath][]-based implementations of the `LinkDiscoverer` interface for the configured hypermedia types. When using this module, an instance supporting this hypermedia type is exposed as a Spring bean. 
<br/><br/>Alternatively, you can setup and use an instance as follows:

```
String content = "{'_links' :  { 'foo' : { 'href' : '/foo/bar' }}}";

LinkDiscoverer discoverer = new SirenLinkDiscoverer();
Link link = discoverer.findLinkWithRel("foo", content);

assertThat(link.getRel(), is("foo"));
assertThat(link.getHref(), is("/foo/bar"));
```

## License
This code is open source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).

[Spring HATEOAS]: https://docs.spring.io/spring-hateoas/docs/current/reference/html/
[Spring HATEOAS Representation Model]: https://docs.spring.io/spring-hateoas/docs/current/reference/html/#fundamentals.representation-models
[Siren]: https://github.com/kevinswiber/siren
[Siren Entity]: https://github.com/kevinswiber/siren/blob/master/README.md#entities
[Siren Entity Embedded Link]: https://github.com/kevinswiber/siren/blob/master/README.md#embedded-link
[Siren Entity Embedded Representation]: https://github.com/kevinswiber/siren/blob/master/README.md#embedded-representation
[Siren Entity Class]: https://github.com/kevinswiber/siren/blob/master/README.md#class
[JSONPath]: https://github.com/json-path/JsonPath
