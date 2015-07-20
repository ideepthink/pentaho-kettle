/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.dimensionlookup;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseInterfaceExtended;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepPartitioningMeta;

import java.sql.Connection;
import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class DimensionLookupTest {
  private DatabaseMeta databaseMeta;
  private DatabaseInterfaceExtended databaseInterface;

  private StepMeta stepMeta;

  private DimensionLookup dimensionLookup, dimensionLookupSpy;
  private DimensionLookupMeta dimensionLookupMeta;
  private DimensionLookupData dimensionLookupData;

  @Before
  public void setUp() throws Exception {
    databaseMeta = mock( DatabaseMeta.class );
    databaseInterface = mock( DatabaseInterfaceExtended.class );
    doReturn( databaseInterface ).when( databaseMeta ).getDatabaseInterface();
    doReturn( "" ).when( databaseMeta ).quoteField( anyString() );

    dimensionLookupMeta = mock( DimensionLookupMeta.class );
    doReturn( databaseMeta ).when( dimensionLookupMeta ).getDatabaseMeta();
    doReturn( new String[]{} ).when( dimensionLookupMeta ).getKeyLookup();
    doReturn( new String[]{} ).when( dimensionLookupMeta ).getFieldLookup();
    doReturn( new int[]{} ).when( dimensionLookupMeta ).getFieldUpdate();

    stepMeta = mock( StepMeta.class );
    doReturn( "step" ).when( stepMeta ).getName();
    doReturn( mock( StepPartitioningMeta.class ) ).when( stepMeta ).getTargetStepPartitioningMeta();
    doReturn( dimensionLookupMeta ).when( stepMeta ).getStepMetaInterface();

    Database db = mock( Database.class );
    doReturn( mock( Connection.class ) ).when( db ).getConnection();

    dimensionLookupData = mock( DimensionLookupData.class );
    dimensionLookupData.db = db;
    dimensionLookupData.keynrs = new int[] { };
    dimensionLookupData.fieldnrs = new int[] { };

    TransMeta transMeta = mock( TransMeta.class );
    doReturn( stepMeta ).when( transMeta ).findStep( anyString() );

    dimensionLookup = new DimensionLookup( stepMeta, dimensionLookupData, 1, transMeta, mock( Trans.class ) );
    dimensionLookup.setData( dimensionLookupData );
    dimensionLookup.setMeta( dimensionLookupMeta );
    dimensionLookupSpy = spy( dimensionLookup );
    doReturn( stepMeta ).when( dimensionLookupSpy ).getStepMeta();
    doReturn( false ).when( dimensionLookupSpy ).isRowLevel();
    doReturn( false ).when( dimensionLookupSpy ).isDebug();
    doReturn( true ).when( dimensionLookupSpy ).isAutoIncrement();
    doNothing().when( dimensionLookupSpy ).logDetailed( anyString() );
  }

  @Test
  public void testDimInsert() throws Exception {
    dimensionLookupSpy.dimInsert( any( RowMetaInterface.class ), any( Object[].class ), anyLong(), anyBoolean(),
        anyLong(), any( Date.class ), any( Date.class ) );
    verify( databaseInterface, times( 2 ) ).supportsAutoGeneratedKeys();
  }
}
