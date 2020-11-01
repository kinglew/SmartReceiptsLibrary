package co.smartreceipts.android.workers.widget

import co.smartreceipts.analytics.Analytics
import co.smartreceipts.analytics.events.Events
import co.smartreceipts.android.model.Trip
import co.smartreceipts.android.widget.tooltip.report.generate.GenerateInfoTooltipManager
import co.smartreceipts.android.workers.EmailAssistant
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class GenerateReportPresenterTest {

    // Class under test
    private lateinit var presenter: GenerateReportPresenter

    private val view = mock<GenerateReportView>()
    private val interactor = mock<GenerateReportInteractor>()

    private val analytics = mock<Analytics>()
    private val generateInfoTooltipManager = mock<GenerateInfoTooltipManager>()
    private val trip = mock<Trip>()

    @Before
    fun setUp() {
        whenever(view.generateReportClicks).thenReturn(Observable.never())
        whenever(interactor.isLandscapeReportEnabled()).thenReturn(false)

        presenter = GenerateReportPresenter(view, interactor, analytics, generateInfoTooltipManager)
    }

    @Test(expected = IllegalStateException::class)
    fun throwExceptionWithoutTrip() {
        presenter.subscribe()
    }

    @Test
    fun isLandscapeReportEnabledTest() {
        whenever(interactor.isLandscapeReportEnabled()).thenReturn(true)

        assertTrue(presenter.isLandscapeReportEnabled())
    }

    @Test
    fun generateReportClickTest() {
        val options: EnumSet<EmailAssistant.EmailOptions> = EnumSet.of(
            EmailAssistant.EmailOptions.PDF_FULL,
            EmailAssistant.EmailOptions.ZIP
        )
        val success = EmailResult.Success(mock())

        whenever(view.generateReportClicks).thenReturn(Observable.just(options))
        whenever(interactor.generateReport(trip, options)).thenReturn(Single.just(success))

        presenter.subscribe(trip)

        verify(view).present(EmailResult.InProgress)
        verify(analytics).record(Events.Generate.GenerateReports)
        verify(generateInfoTooltipManager).reportWasGenerated()

        verify(analytics).record(Events.Generate.FullPdfReport)
        verify(analytics).record(Events.Generate.ZipReport)

        verify(interactor).generateReport(trip, options)

        verify(view).present(success)
    }
}