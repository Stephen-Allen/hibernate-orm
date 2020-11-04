/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.procedure.internal;

import java.util.function.Consumer;

import org.hibernate.NotYetImplementedFor6Exception;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.query.internal.ResultSetMappingResolutionContext;
import org.hibernate.query.named.NamedObjectRepository;
import org.hibernate.query.named.NamedResultSetMappingMemento;
import org.hibernate.query.results.ResultSetMapping;

import org.jboss.logging.Logger;

/**
 * Utilities used to implement procedure call support.
 *
 * @author Steve Ebersole
 */
public class Util {
	private static final Logger log = Logger.getLogger( Util.class );

	private Util() {
	}

	public static void resolveResultSetMappings(
			String[] resultSetMappingNames,
			Class[] resultSetMappingClasses,
			ResultSetMapping resultSetMapping,
			Consumer<String> querySpaceConsumer,
			ResultSetMappingResolutionContext context) {
		if ( ! ArrayHelper.isEmpty( resultSetMappingNames ) ) {
			// cannot specify both
			if ( ! ArrayHelper.isEmpty( resultSetMappingClasses ) ) {
				throw new IllegalArgumentException( "Cannot specify both result-set mapping names and classes" );
			}
			resolveResultSetMappingNames( resultSetMappingNames, resultSetMapping, querySpaceConsumer, context );
		}
		else if ( ! ArrayHelper.isEmpty( resultSetMappingClasses ) ) {
			resolveResultSetMappingClasses( resultSetMappingClasses, resultSetMapping, querySpaceConsumer, context );
		}

		// otherwise, nothing to resolve
	}

	public static void resolveResultSetMappingNames(
			String[] resultSetMappingNames,
			ResultSetMapping resultSetMapping,
			Consumer<String> querySpaceConsumer,
			ResultSetMappingResolutionContext context) {
		final NamedObjectRepository namedObjectRepository = context.getSessionFactory().getQueryEngine().getNamedObjectRepository();

		for ( String resultSetMappingName : resultSetMappingNames ) {
			final NamedResultSetMappingMemento memento = namedObjectRepository.getResultSetMappingMemento( resultSetMappingName );
			memento.resolve(
					resultSetMapping,
					querySpaceConsumer,
					context
			);
		}
	}

	public static void resolveResultSetMappingClasses(
			Class[] resultSetMappingClasses,
			ResultSetMapping resultSetMapping,
			Consumer<String> querySpaceConsumer,
			ResultSetMappingResolutionContext context) {
		throw new NotYetImplementedFor6Exception( Util.class );

//		final DomainMetamodel domainModel = sessionFactory.getDomainModel();
//		final TypeConfiguration typeConfiguration = domainModel.getTypeConfiguration();
//
//		for ( Class resultSetMappingClass : resultSetMappingClasses ) {
//			final BasicType basicType = typeConfiguration.getBasicTypeForJavaType( resultSetMappingClass );
//			if ( basicType != null ) {
//				//noinspection unchecked
//				resultProducerConsumer.accept( new ScalarDomainResultProducer<>( basicType ) );
//				continue;
//			}
//
//			final EntityPersister entityDescriptor = domainModel.findEntityDescriptor( resultSetMappingClass );
//			if ( entityDescriptor != null ) {
//				resultProducerConsumer.accept( new Entity );
//				for ( String querySpace : entityDescriptor.getSynchronizedQuerySpaces() ) {
//					querySpaceConsumer.accept( querySpace );
//				}
//			}
//		}
	}
}
